package appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model;

/**
 * Created by econecta on 07/04/17.
 */

public class Restaurantes {
    private String Nome;
    private String Descricao;
    private double Latitude;
    private double Longitude;

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        this.Descricao = descricao;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
