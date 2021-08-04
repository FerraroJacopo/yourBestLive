package dev.peihana.yourbestlive;


import android.widget.RatingBar;
import android.widget.TextView;

public class Post {

    public TextView preferiti_add;
    public String imageUrl;
    public String nome_locale;
    public RatingBar stelline;
    public float rate;
    public float iterazioni;
;

    public Post(String imageUrl, String nome_locale, RatingBar stelline,float rate,float iterazioni,TextView preferiti) {
        this.imageUrl = imageUrl;
        this.nome_locale = nome_locale;
        this.stelline = stelline;
        this.rate=rate;
        this.iterazioni=iterazioni;
        this.preferiti_add=preferiti;
    }

    public Post() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNome_locale() {
        return nome_locale;
    }

    public void setNome_locale(String nome_locale) {
        this.nome_locale = nome_locale;
    }

    public RatingBar getStelline() {
        return stelline;
    }

    public void setStelline(RatingBar stelline) {
        this.stelline = stelline;
    }

    public float getRate() { return rate; }

    public void setRate(float rate) { this.rate = rate; }

    public float getIterazioni() { return iterazioni; }

    public void setPreferiti(TextView preferiti_add,String string) {   preferiti_add.setText(string);     }

    public void setIterazioni(float iterazioni) { this.iterazioni = iterazioni; }




}