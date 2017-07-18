package appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by econecta on 07/04/17.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;

    public static DatabaseReference getFirebase(){

        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }



}
