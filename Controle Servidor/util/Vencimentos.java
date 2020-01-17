/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author Guilherme
 */
public enum Vencimentos {

    /**
     *
     */
    JUNHO15("15/06/2015", "05/06/2015"),

    /**
     *
     */
    JUNHO22("22/06/2015", "12/06/2015"),

    /**
     *
     */
    JUNHO29("29/06/2015", "19/06/2015"),

    /**
     *
     */
    JULHO06("06/07/2015", "26/06/2015"),

    /**
     *
     */
    JULHO13("13/07/2015", "02/07/2015"),

    /**
     *
     */
    JULHO20("20/07/2015", "10/07/2015"),

    /**
     *
     */
    JULHO27("27/07/2015", "17/07/2015"),

    /**
     *
     */
    AGOSTO03("03/08/2015", "24/07/2015"),

    /**
     *
     */
    AGOSTO10("10/08/2015", "30/07/2015"),

    /**
     *
     */
    AGOSTO17("17/08/2015", "07/08/2015"),

    /**
     *
     */
    AGOSTO24("24/08/2015", "14/08/2015"),

    /**
     *
     */
    AGOSTO31("31/08/2015", "21/08/2015"),

    /**
     *
     */
    SETEMBRO08("08/09/2015", "28/08/2015"),

    /**
     *
     */
    SETEMBRO14("14/09/2015", "03/09/2015"),

    /**
     *
     */
    SETEMBRO21("21/09/2015", "11/09/2015"),

    /**
     *
     */
    SETEMBRO28("28/09/2015", "18/09/2015"),

    /**
     *
     */
    OUTUBRO05("05/10/2015", "25/09/2015"),

    /**
     *
     */
    OUTUBRO13("13/10/2015", "02/10/2015"),

    /**
     *
     */
    OUTUBRO19("19/10/2015", "09/10/2015"),

    /**
     *
     */
    OUTUBRO26("26/10/2015", "16/10/2015"),

    /**
     *
     */
    NOVEMBRO03("03/11/2015", "23/10/2015"),

    /**
     *
     */
    NOVEMBRO09("09/11/2015", "29/10/2015"),

    /**
     *
     */
    NOVEMBRO16("16/11/2015", "06/11/2015"),

    /**
     *
     */
    NOVEMBRO23("23/11/2015", "12/11/2015"),

    /**
     *
     */
    NOVEMBRO30("30/11/2015", "19/11/2015"),

    /**
     *
     */
    DEZEMBRO07("07/12/2015", "27/11/2015"),

    /**
     *
     */
    DEZEMBRO16("16/12/2015", "08/12/2015"),

    /**
     *
     */
    JANEIRO06("06/01/2016", "16/12/2015"),

    /**
     *
     */
    JANEIRO12("12/01/2016", "04/01/2016"),

    /**
     *
     */
    JANEIRO18("18/01/2016", "08/01/2016"),

    /**
     *
     */
    JANEIRO26("26/01/2016", "15/01/2016"),

    /**
     *
     */
    FEVEREIRO01("01/02/2016","01/02/2016"),

    /**
     *
     */
    FEVEREIRO05("05/02/2016","01/02/2016"),

    /**
     *
     */
    FEVEREIRO16("16/02/2016","01/02/2016"),

    /**
     *
     */
    FEVEREIRO22("22/02/2016","01/02/2016"),

    /**
     *
     */
    FEVEREIRO26("26/02/2016","01/02/2016"),

    /**
     *
     */
    MARCO07("07/03/2016","25/02/2016"),

    /**
     *
     */
    MARCO14("14/03/2016","04/03/2016"),

    /**
     *
     */
    MARCO21("21/03/2016","11/03/2016"),

    /**
     *
     */
    MARCO30("30/03/2016","16/03/2016"),

    /**
     *
     */
    ALL("01/01/2016", "-");

    public String vencimento, envio;

    Vencimentos(String arg0, String arg1) {
        this.vencimento = arg0;
        this.envio = arg1;
    }

    /**
     *
     * @return
     */
    public String getValor() {
        return vencimento;
    }

    /**
     *
     * @return
     */
    public String getEnvio() {
        return envio;
    }

    /**
     *
     * @param valor
     * @return
     */
    public static Vencimentos getVencimento(String valor) {
        Vencimentos[] vencimentos = Vencimentos.values();

        for (Vencimentos vencimento : vencimentos) {
            if (vencimento.getValor().equals(valor)) {
                return vencimento;
            }
        }

        return null;
    }

    /**
     *
     * @param valor
     * @return
     */
    public static Vencimentos getEnvio(String valor) {
        Vencimentos[] vencimentos = Vencimentos.values();

        for (Vencimentos vencimento : vencimentos) {
            if (vencimento.getEnvio().equals(valor)) {
                return vencimento;
            }
        }

        return null;
    }

}
