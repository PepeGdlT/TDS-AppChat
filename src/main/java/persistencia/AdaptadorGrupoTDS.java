package persistencia;

import java.util.*;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupoTDS implements IAdaptadorGrupoDAO {

    private static final String GRUPO = "grupo";
    private static final String NOMBRE = "nombre";
    private static final String MIEMBROS = "miembros";
    private static final String ADMINISTRADOR = "administrador";
    private static final String MENSAJES = "mensajes";

    private static ServicioPersistencia servPersistencia;
    private static FactoriaDAO factoria;
    private static final Set<Integer> gruposEnRecuperacion = new HashSet<>(); // Prevent infinite recursion

    public AdaptadorGrupoTDS() throws DAOException {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        factoria = FactoriaDAO.getUnicaInstancia();
    }

    @Override
    public void registrarGrupo(Grupo grupo) {
        if (grupo == null || existeGrupo(grupo.getCodigo())) return;

        Entidad eGrupo = new Entidad();
        eGrupo.setNombre(GRUPO);
        eGrupo.setPropiedades(List.of(
                new Propiedad(NOMBRE, grupo.getNombreContacto()),
                new Propiedad(ADMINISTRADOR, String.valueOf(grupo.getAdministrador().getCodigo())),
                new Propiedad(MIEMBROS, obtenerCodigosMiembros(grupo.getMiembros())),
                new Propiedad(MENSAJES, obtenerCodigosMensajes(grupo.getMensajesEnviados()))
        ));

        eGrupo = servPersistencia.registrarEntidad(eGrupo);
        grupo.setCodigo(eGrupo.getId());

        PoolDAO.INSTANCE.addObjeto(grupo.getCodigo(), grupo);

        registrarMiembros(grupo.getMiembros());
        registrarMensajes(grupo.getMensajesEnviados());
    }

    @Override
    public void borrarGrupo(Grupo grupo) {
        if (grupo == null) return;

        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
        borrarMensajes(grupo.getMensajesEnviados());
        servPersistencia.borrarEntidad(eGrupo);

        PoolDAO.INSTANCE.removeObjeto(grupo.getCodigo());
    }

    @Override
    public void modificarGrupo(Grupo grupo) {
        if (grupo == null) return;

        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
        actualizarPropiedadesGrupo(eGrupo, grupo);
    }

    @Override
    public Grupo recuperarGrupo(int codigo) {
        if (PoolDAO.INSTANCE.contiene(codigo)) {
            return (Grupo) PoolDAO.INSTANCE.getObjeto(codigo);
        }

        // Prevent infinite recursion
        if (gruposEnRecuperacion.contains(codigo)) {
            System.err.println("Warning: Grupo " + codigo + " is already being recovered. Avoiding infinite loop.");
            return null;
        }
        gruposEnRecuperacion.add(codigo);

        Grupo grupo = null;
        try {
            Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
            if (eGrupo == null) {
                System.err.println("Error: Grupo " + codigo + " not found.");
                return null;
            }

            String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
            int codigoAdmin = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eGrupo, ADMINISTRADOR));

            Usuario administrador = factoria.getUsuarioDAO().recuperarUsuario(codigoAdmin);
            if (administrador == null) {
                System.err.println("Error: Grupo " + codigo + " has a missing administrator (ID: " + codigoAdmin + ")");
                return null;
            }

            List<ChatIndividual> miembros = obtenerMiembrosDesdeCodigos(
                    servPersistencia.recuperarPropiedadEntidad(eGrupo, MIEMBROS));

            List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(
                    servPersistencia.recuperarPropiedadEntidad(eGrupo, MENSAJES));

            grupo = new Grupo(nombre, mensajes, miembros, administrador);
            grupo.setCodigo(codigo);

            PoolDAO.INSTANCE.addObjeto(codigo, grupo);
        } finally {
            gruposEnRecuperacion.remove(codigo);
        }

        return grupo;
    }

    @Override
    public List<Grupo> recuperarTodosGrupos() {
        return servPersistencia.recuperarEntidades(GRUPO).stream()
                .map(entidad -> recuperarGrupo(entidad.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // --------------------------------------------------------------------------------

    private boolean existeGrupo(int codigo) {
        return servPersistencia.recuperarEntidad(codigo) != null;
    }

    private void actualizarPropiedadesGrupo(Entidad eGrupo, Grupo grupo) {
        for (Propiedad prop : eGrupo.getPropiedades()) {
            switch (prop.getNombre()) {
                case NOMBRE -> prop.setValor(grupo.getNombreContacto());
                case ADMINISTRADOR -> prop.setValor(String.valueOf(grupo.getAdministrador().getCodigo()));
                case MIEMBROS -> prop.setValor(obtenerCodigosMiembros(grupo.getMiembros()));
                case MENSAJES -> prop.setValor(obtenerCodigosMensajes(grupo.getMensajesEnviados()));
            }
            servPersistencia.modificarPropiedad(prop);
        }
    }

    private void registrarMiembros(List<ChatIndividual> miembros) {
        AdaptadorChatIndividualTDS adaptadorChatIndividual = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        miembros.forEach(adaptadorChatIndividual::registrarChatIndividual);
    }

    private void registrarMensajes(List<Mensaje> mensajes) {
        AdaptadorMensajeTDS adaptadorMensaje = (AdaptadorMensajeTDS) factoria.getMensajeDAO();
        mensajes.forEach(adaptadorMensaje::registrarMensaje);
    }

    private void borrarMensajes(List<Mensaje> mensajes) {
        AdaptadorMensajeTDS adaptadorMensaje = (AdaptadorMensajeTDS) factoria.getMensajeDAO();
        mensajes.forEach(adaptadorMensaje::borrarMensaje);
    }

    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        if (codigos == null || codigos.isEmpty()) return new ArrayList<>();

        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorMensajeTDS adaptadorMensaje = (AdaptadorMensajeTDS) factoria.getMensajeDAO();
        List<Mensaje> mensajes = new ArrayList<>();

        while (strTok.hasMoreTokens()) {
            mensajes.add(adaptadorMensaje.recuperarMensaje(Integer.parseInt(strTok.nextToken())));
        }
        return mensajes;
    }

    private List<ChatIndividual> obtenerMiembrosDesdeCodigos(String codigos) {
        if (codigos == null || codigos.isEmpty()) return new ArrayList<>();

        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorChatIndividualTDS adaptadorChatIndividual = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        List<ChatIndividual> miembros = new ArrayList<>();

        while (strTok.hasMoreTokens()) {
            miembros.add(adaptadorChatIndividual.recuperarChatIndividual(Integer.parseInt(strTok.nextToken())));
        }
        return miembros;
    }

    private String obtenerCodigosMiembros(List<ChatIndividual> miembros) {
        return miembros.stream()
                .map(m -> String.valueOf(m.getCodigo()))
                .collect(Collectors.joining(" "));
    }

    private String obtenerCodigosMensajes(List<Mensaje> mensajes) {
        return mensajes.stream()
                .map(m -> String.valueOf(m.getCodigo()))
                .collect(Collectors.joining(" "));
    }
}
