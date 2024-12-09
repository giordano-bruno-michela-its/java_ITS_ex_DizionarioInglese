package model;

public class Definizione {
    
    private int id;
    private String vocaboloInglese;
    private String traduzioneItaliana;
    private String fraseInglese;
    private String traduzioneFrase;

    public Definizione(int id, String vocaboloInglese, String traduzioneItaliana, String fraseInglese, String traduzioneFrase) {
        this.id = id;
        this.vocaboloInglese = vocaboloInglese;
        this.traduzioneItaliana = traduzioneItaliana;
        this.fraseInglese = fraseInglese;
        this.traduzioneFrase = traduzioneFrase;
    }

    public int getId() {
        return id;
    }

    public String getVocaboloInglese() {
        return vocaboloInglese;
    }

    public String getTraduzioneItaliana() {
        return traduzioneItaliana;
    }


    public String getFraseInglese() {
        return fraseInglese;
    }

    public String getTraduzioneFrase() {
        return traduzioneFrase;
    }
}
