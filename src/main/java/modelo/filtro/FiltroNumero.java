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
        boolean coincideReceptor = false;
        Contacto receptor = mensaje.getReceptor();
		if (receptor instanceof ChatIndividual) {
			 coincideReceptor = ((ChatIndividual) receptor).getnumeroTelefono().contains(criterio);
		} else if (receptor instanceof Grupo) {
			return false; 
		}
       
        boolean coincideEmisor = mensaje.getEmisor().getNumeroTelefono().contains(criterio);
        
        return coincideReceptor || coincideEmisor;
    }
}
