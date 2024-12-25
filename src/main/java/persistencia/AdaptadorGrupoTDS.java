package persistencia;

import java.util.ArrayList;
import java.util.LinkedList;
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
                new Propiedad(NOMBRE, grupo.getNombreContacto()),
                new Propiedad(ADMINISTRADOR, String.valueOf(grupo.getAdministrador().getCodigo())),
                new Propiedad(MIEMBROS, obtenerCodigosMiembros(grupo.getMiembros())),
                new Propiedad(MENSAJES, obtenerCodigosMensajes(grupo.getMensajesEnviados()))
        )));

        eGrupo = servPersistencia.registrarEntidad(eGrupo);
        grupo.setCodigo(eGrupo.getId());

        PoolDAO.INSTANCE.addObjeto(grupo.getCodigo(), grupo);

        // Registrar miembros y mensajes
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

        Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
        String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
        Usuario administrador = factoria.getUsuarioDAO().recuperarUsuario(
                Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eGrupo, ADMINISTRADOR)));
        
        Grupo grupo = new Grupo(nombre, new LinkedList<>(), new LinkedList<>(), administrador);
        grupo.setCodigo(codigo);

        grupo.setMensajesEnviados(obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, MENSAJES)));
        grupo.setMiembros(obtenerMiembrosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, MIEMBROS)));

        PoolDAO.INSTANCE.addObjeto(codigo, grupo);
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
    	
		for (Propiedad prop : eGrupo.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombreContacto());
			} else if (prop.getNombre().equals(ADMINISTRADOR)) {
				prop.setValor(String.valueOf(grupo.getAdministrador().getCodigo()));
			} else if (prop.getNombre().equals(MIEMBROS)) {
				prop.setValor(obtenerCodigosMiembros(grupo.getMiembros()));
			} else if (prop.getNombre().equals(MENSAJES)) {
				prop.setValor(obtenerCodigosMensajes(grupo.getMensajesEnviados()));
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
