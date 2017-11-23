package android.lifeistech.com.braillewords;

public class Braille {

    private String code;
    private int weight;
    private char c_japanese;
    private String s_japanese;
    private int res;
    private String number;

    private int res1;
    private int res2;

    Braille() {
    }

    Braille(int res1,int res2, char japanese) {
        this.res1 = res1;
        this.res2 = res2;
        this.c_japanese = japanese;
    }

    public int getRes1() {
        return res1;
    }

    public int getRes2() {
        return res2;
    }


    Braille(int weight, String number) {
        this.number = number;
        this.weight = weight;
    }

    public String getNumber() {
        return number;
    }
    Braille(String code, char japanese) {
        this.code = code;
        this.c_japanese = japanese;
    }

    Braille(String japanese,int weight) {
        this.s_japanese = japanese;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    Braille(int res, char japanese) {
        this.res = res;
        this.c_japanese = japanese;
    }

    public String getCode() {
        return code;
    }

    public char getC_japanese() {
        return c_japanese;
    }

    public String getS_japanese() {
        return s_japanese;
    }

    public int getRes(){
        return res;
    }
}
