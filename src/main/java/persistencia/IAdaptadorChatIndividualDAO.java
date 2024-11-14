package persistencia;

import java.util.List;

import modelo.ChatIndividual;

public interface IAdaptadorChatIndividualDAO {
	
	// Metodos CRUD - Create, Read, Update, Delete
	 void registrarChatIndividual(ChatIndividual chat);
	 void borrarChatIndividual(ChatIndividual chat);
	 void modificarChatIndividual(ChatIndividual chat);
	 ChatIndividual recuperarChatIndividual(int codigo);
	 List<ChatIndividual> recuperarTodosChatsIndividuales();
}
