package dev.peihana.yourbestlive;



public class Commento {
    public String utente;
    public String commento;
    public float stelle;

    public Commento(String username,String commento,float stelle) {
        this.utente = username;
        this.commento = commento;
        this.stelle=stelle;
    }

    public Commento() {
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public float getStelle() {
        return stelle;
    }

    public void setStelle(float stelle) {
        this.stelle = stelle;
    }
}