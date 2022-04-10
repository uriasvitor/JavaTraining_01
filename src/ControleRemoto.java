public class ControleRemoto implements Controlador{
    private int volume;
    private boolean ligado;
    private boolean tocando;

    public ControleRemoto(){
        this.volume = 5;
        this.ligado = true;
        this.tocando = true;
    }

    private int getVolume(){

        return this.volume;
    }
    private boolean getLigado() {

        return ligado;
    }
    private boolean getTocando() {

        return tocando;
    }

    private void setLigado(boolean ligado) {

        this.ligado = ligado;
    }

    private void setVolume(int volume){
        this.volume = volume;
    }
    private void setTocando(boolean tocando){
        this.tocando = tocando;
    }

    @Override
    public void ligar() {
        setLigado(true);
    }

    @Override
    public void desligar() {
        setTocando(false);
        setLigado(false);
    }

    @Override
    public void abrirMenu() {
        System.out.println("--------- MENU -------------");
        System.out.println("Esta ligado? "+ this.getLigado());

        if(this.getLigado()){
        System.out.println("Esta tocando? "+ this.getTocando());
        System.out.println("Volume : "+ this.getVolume());
            for(int i = 0 ; i <= this.getVolume(); i++){
                System.out.printf("|I|");
            }
        }
    }

    @Override
    public void fecharMenu() {
        System.out.println("\n ---- Fechando menu..... ----------");
    }

    @Override
    public void maisVolume() {
        if(this.getLigado()){
            this.setVolume(this.getVolume() + 5);
        }
    }

    @Override
    public void menosVolume() {
        if(this.getLigado()){
            this.setVolume((this.getVolume() - 5 ));
        }
    }

    @Override
    public void desligarMudo() {
        if(this.getLigado() && this.getVolume() == 0 ){
            this.setVolume(50);
        }
    }

    @Override
    public void ligarMudo() {
        if(this.getLigado() && this.getVolume() > 0){
            this.setVolume(0);
        }
    }

    @Override
    public void play() {
        if (this.getLigado() && !(this.getTocando())){
            this.setTocando(true);
        }else{
            System.out.println("O controle está desligado");
        }
    }

    @Override
    public void pause() {
        if (this.getLigado() && this.getTocando()){
            this.setTocando(false);
        }
    }
}
