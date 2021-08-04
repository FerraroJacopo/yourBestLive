package dev.peihana.yourbestlive;


public class Preferiti {

    public String nome_locale;
    public String via;
    public String imageUrl;

    public Preferiti() { }


    public Preferiti( String nome_locale,String via,String imageUrl) {
        this.nome_locale = nome_locale;
        this.via = via;
        this.imageUrl = imageUrl;
    }

    public String getNome_locale() {
        return nome_locale;
    }

    public void setNome_locale(String nome_locale) {
        this.nome_locale = nome_locale;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getImageUrl() { return imageUrl;}

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl;}
}

