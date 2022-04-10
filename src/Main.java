public class Main {
    //AQUI É ONDE O PROJETO STARTA (MOSTRA INFORMAÇÕES NO TERMINAL )
    public static void main(String[] args) {
        ControleRemoto controle = new ControleRemoto();
        controle.maisVolume();
        controle.ligar();
        controle.ligarMudo();
        controle.abrirMenu();
        controle.fecharMenu();
    }
}
