package persistencia;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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

    AdaptadorGrupoTDS() throws DAOException {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        factoria = FactoriaDAO.getUnicaInstancia();
    }

    @Override
    public void registrarGrupo(Grupo grupo) {
        if (grupo == null || existeGrupo(grupo.getCodigo())) return;

        Entidad eGrupo = new Entidad();
        eGrupo.setNombre(GRUPO);
        eGrupo.setPropiedades(new ArrayList<>(List.of(
                new Propiedad(NOMBRE, grupo.getNombre()),
                new Propiedad(ADMINISTRADOR, String.valueOf(grupo.getAdministrador().getCodigo())),
                new Propiedad(MIEMBROS, obtenerCodigosMiembros(grupo.getListaMiembros())),
                new Propiedad(MENSAJES, obtenerCodigosMensajes(grupo.getMensajesEnviados()))
        )));

        eGrupo = servPersistencia.registrarEntidad(eGrupo);
        grupo.setCodigo(eGrupo.getId());

        PoolDAO.getUnicaInstancia().addObjeto(grupo.getCodigo(), grupo);

        // Registrar miembros y mensajes
        registrarMiembros(grupo.getListaMiembros());
        registrarMensajes(grupo.getMensajesEnviados());
    }

    @Override
    public void borrarGrupo(Grupo grupo) {
        if (grupo == null) return;

        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
        borrarMensajes(grupo.getMensajesEnviados());
        servPersistencia.borrarEntidad(eGrupo);

        PoolDAO.getUnicaInstancia().removeObjeto(grupo.getCodigo());
    }

    @Override
    public void modificarGrupo(Grupo grupo) {
        if (grupo == null) return;

        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
        actualizarPropiedadesGrupo(eGrupo, grupo);
    }

    @Override
    public Grupo recuperarGrupo(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
        String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
        Usuario administrador = factoria.getUsuarioDAO().recuperarUsuario(
                Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eGrupo, ADMINISTRADOR)));
        
        Grupo grupo = new Grupo(nombre, new ArrayList<>(), new ArrayList<>(), administrador);
        grupo.setCodigo(codigo);

        grupo.setMensajesEnviados(obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, MENSAJES)));
        grupo.setListaMiembros(obtenerMiembrosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, MIEMBROS)));

        PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);
        return grupo;
    }

    @Override
    public List<Grupo> recuperarTodosGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades(GRUPO);
        for (Entidad eGrupo : entidades) {
            grupos.add(recuperarGrupo(eGrupo.getId()));
        }
        return grupos;
    }

    // --------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // --------------------------------------------------------------------------------

    private boolean existeGrupo(int codigo) {
        return servPersistencia.recuperarEntidad(codigo) != null;
    }

    private void actualizarPropiedadesGrupo(Entidad eGrupo, Grupo grupo) {
        servPersistencia.eliminarPropiedadEntidad(eGrupo, NOMBRE);
        servPersistencia.anadirPropiedadEntidad(eGrupo, NOMBRE, grupo.getNombre());

        servPersistencia.eliminarPropiedadEntidad(eGrupo, ADMINISTRADOR);
        servPersistencia.anadirPropiedadEntidad(eGrupo, ADMINISTRADOR, String.valueOf(grupo.getAdministrador().getCodigo()));

        servPersistencia.eliminarPropiedadEntidad(eGrupo, MIEMBROS);
        servPersistencia.anadirPropiedadEntidad(eGrupo, MIEMBROS, obtenerCodigosMiembros(grupo.getListaMiembros()));

        servPersistencia.eliminarPropiedadEntidad(eGrupo, MENSAJES);
        servPersistencia.anadirPropiedadEntidad(eGrupo, MENSAJES, obtenerCodigosMensajes(grupo.getMensajesEnviados()));
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
        List<Mensaje> mensajes = new ArrayList<>();
        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorMensajeTDS adaptadorMensaje = (AdaptadorMensajeTDS) factoria.getMensajeDAO();
        while (strTok.hasMoreTokens()) {
            mensajes.add(adaptadorMensaje.recuperarMensaje(Integer.parseInt(strTok.nextToken())));
        }
        return mensajes;
    }

    private List<ChatIndividual> obtenerMiembrosDesdeCodigos(String codigos) {
        List<ChatIndividual> miembros = new ArrayList<>();
        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorChatIndividualTDS adaptadorChatIndividual = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
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
