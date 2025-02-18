package modelo.filtro;

import modelo.Mensaje;
import modelo.Contacto;
import modelo.Grupo;
import modelo.ChatIndividual;

public class FiltroNumero extends FiltroBase {
    public FiltroNumero(String criterio) {
        super(criterio);
    }

    @Override
    protected boolean cumpleCriterio(Mensaje mensaje) {
        Contacto receptor = mensaje.getReceptor();
        
        if (receptor instanceof ChatIndividual) {
            return ((ChatIndividual) receptor).getnumeroTelefono().contains(criterio);
        } else if (receptor instanceof Grupo) {
            return ((Grupo) receptor).getAdministrador().getNumeroTelefono().contains(criterio);
        }
        
        return false;
    }
}
