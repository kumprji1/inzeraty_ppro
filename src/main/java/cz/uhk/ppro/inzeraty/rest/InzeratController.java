package cz.uhk.ppro.inzeraty.rest;

import cz.uhk.ppro.inzeraty.model.Inzerat;
import cz.uhk.ppro.inzeraty.sluzby.PametoveUlozisteInzeratu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
* Za použití Postmanu ověřeny všechny API endpointy, v reactu jsem narazil na CORS, které jsem prozatím nevyřešil
*/

@RestController
public class InzeratController {

    @Autowired
    private PametoveUlozisteInzeratu uloziste;

    @GetMapping("/inzeraty")
    public String getInzeraty() {
        return uloziste.getInzeraty().toString();
    }

    @PostMapping("/add-inzerat")
    public Inzerat addInzerat(
            @RequestParam("cena") BigDecimal cena,
            @RequestParam("kategorie") String kategorie,
            @RequestParam("text") String text) {
        Inzerat i = new Inzerat();
        i.setCena(cena);
        i.setKategorie(kategorie);
        i.setText(text);
        uloziste.pridej(i);
        return i;
    }

    @DeleteMapping("/delete-inzerat")
    public String deleteInzerat(@RequestParam("id") Integer id, @RequestParam("heslo") String heslo) {
        Inzerat i = uloziste.getById(id);
        if (i.getHesloProUpravu().equals(heslo)) {
            uloziste.odstran(i);
            return "Odstaněno";
        }
        return "Nepodařilo se odstranit";
    }

    @PatchMapping("/update-inzerat")
    public String updateInzerat(@RequestParam("id") Integer id, @RequestParam("heslo") String heslo,
                                @RequestParam("text") String text, @RequestParam("cena") BigDecimal cena,
                                @RequestParam("kategorie") String kategorie) {
        Inzerat i = uloziste.getById(id);
        if (i.getHesloProUpravu().equals(heslo)) {
            if (!text.equals(""))
                i.setText(text);
            if (!cena.equals(""))
                i.setCena(cena);
            if (!kategorie.equals(""))
                i.setKategorie(kategorie);
            return "Upraveno";
        }
        return "Nepodařilo se upravit inzerát";
    }

    @GetMapping("/inzeraty-kategorie")
    public String getByCategory(@RequestParam("kategorie") String kategorie) {
        List<Inzerat> inzeratyKategorie = uloziste.getInzeratyByKategorie(kategorie);
        return inzeratyKategorie.toString();
    }
}
