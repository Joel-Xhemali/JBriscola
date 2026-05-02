import it.com.jbriscola.controller.ControllerGioco;
import it.com.jbriscola.model.GiocoBriscola;
import it.com.jbriscola.view.FinestraGioco;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        GiocoBriscola gioco = new GiocoBriscola();
        FinestraGioco vista = new FinestraGioco();
        new ControllerGioco(gioco, vista);
    }
}