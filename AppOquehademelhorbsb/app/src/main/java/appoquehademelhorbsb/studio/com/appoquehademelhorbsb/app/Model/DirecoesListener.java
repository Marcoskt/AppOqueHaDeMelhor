package appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model;

import java.util.List;

/**
 * Created by econecta on 07/04/17.
 */

public interface DirecoesListener {
    void onDirecoesStart();
    void onDirecoesSuccess(List<Rotas> route);
}
