package brunoscrokbrunoro.firebase_talk.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Scrok Brunoro
 * @create 8/8/16 20:21
 * @project Firebase-Talk
 */
public class Mensagem {

    private String uid;
    private String usuario;
    private String mensagem;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("mensagem", mensagem);
        result.put("usuario", usuario);

        return result;
    }

}
