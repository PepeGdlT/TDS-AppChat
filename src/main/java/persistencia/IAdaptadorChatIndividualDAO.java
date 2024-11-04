package persistencia;

import java.util.List;

import modelo.ChatIndividual;

public interface IAdaptadorChatIndividualDAO {
	
	// Metodos CRUD - Create, Read, Update, Delete
	public void registrarChatIndividual(ChatIndividual chat);
	public void borrarChatIndividual(ChatIndividual chat);
	public void modificarChatIndividual(ChatIndividual chat);
	public ChatIndividual recuperarChatIndividual(int codigo);
	public List<ChatIndividual> recuperarTodosChatsIndividuales();
}
