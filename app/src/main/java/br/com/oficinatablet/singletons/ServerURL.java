package br.com.oficinatablet.singletons;

/**
 * Created by Mateus Emanuel Araújo on 9/13/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class ServerURL {
    private static ServerURL ourInstance = new ServerURL();

    public static ServerURL getInstance() {
        return ourInstance;
    }

    private ServerURL() {
    }

    public String baseUrl() {
        return "https://oficinatablet-84882.firebaseio.com";
    }

    /**
     * @return url onde esta armazenado todos os grupos de chat criados com suas respectivas descrições
     */
    public String chatsUrl() {
        return "chats";
    }

    /**
     * @return url dos membros associados ao chate especificado
     */
    public String chatMembersUrl() {
        return "members";
    }

    /**
     * @return url das mensagens associadas ao chat especificado
     */
    public String chatMessagesUrl() {
        return "messages";
    }

    /**
     * @return url da lista de usuarios cadastrados
     */
    public String usersUrl() {
        return "users";
    }

    private String formatURL(String urlBase, String directory) {
        return String.format("%s/%s", urlBase, directory);
    }
}
