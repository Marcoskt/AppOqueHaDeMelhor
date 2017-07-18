package appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by econecta on 07/04/17.
 */

public class Rotas {
    public Distancia distancia;
    public Duracao duracao;
    public String enderecoFinal;
    public LatLng localFinal;
    public String enderecoInicial;
    public LatLng localInicial;

    public List<LatLng> points;
}
