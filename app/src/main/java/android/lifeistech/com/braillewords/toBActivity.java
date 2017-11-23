package android.lifeistech.com.braillewords;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.Transliterator;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class toBActivity extends AppCompatActivity implements TextWatcher {
    private static final int REQUEST_CODE = 1000;
    private Button voice;
    private int lang;

    EditText toB_; //翻訳する原文

    ImageView[] imageView;
    TextView textView2;
    TextView textView3;
    String voice1;//漢字
    String voice2;//カタカナ
    String voice3;//全角
    String voice4;

    public Braille[] brailles;
    public Braille[] brailles2;

    private char[] japaneses = {
            'あ','い','う','え','お',
            'か','き','く','け','こ',
            'さ','し','す','せ','そ',
            'た','ち','つ','て','と',
            'な','に','ぬ','ね','の',
            'は','ひ','ふ','へ','ほ',
            'ま','み','む','め','も',
            'や','ゆ','よ',
            'ら','り','る','れ','ろ',
            'わ','ゐ','ゑ','を',
            'ん','っ','ー',
            '。','、',/*'・',*/'？','！'
    };

    private int[] res = {
            R.drawable.a_1,R.drawable.i_3,R.drawable.u_9,R.drawable.e_11,R.drawable.o_10,
            R.drawable.ka_33,R.drawable.ki_35,R.drawable.ku_41,R.drawable.ke_43,R.drawable.ko_42,
            R.drawable.sa_49,R.drawable.shi_51,R.drawable.su_57,R.drawable.se_59,R.drawable.so_58,
            R.drawable.ta_21,R.drawable.chi_23,R.drawable.tsu_29,R.drawable.te_31,R.drawable.to_30,
            R.drawable.na_5,R.drawable.ni_7,R.drawable.nu_13,R.drawable.ne_15,R.drawable.no_14,
            R.drawable.ha_37,R.drawable.hi_39,R.drawable.hu_45,R.drawable.he_47,R.drawable.ho_46,
            R.drawable.ma_53,R.drawable.mi_55,R.drawable.mu_61,R.drawable.me_63,R.drawable.mo_62,
            R.drawable.ya_12,R.drawable.yu_44,R.drawable.yo_28,
            R.drawable.ra_17,R.drawable.ri_19,R.drawable.ru_25,R.drawable.re_27,R.drawable.ro_26,
            R.drawable.wa_4,R.drawable.wyi_6,R.drawable.wye_22,R.drawable.wo_20,
            R.drawable.n_52,R.drawable.ltu_2,R.drawable.haihun_18,
            R.drawable.kuten_50,R.drawable.touten_48,/*R.drawable.ten_16,*/R.drawable.question_34,R.drawable.exclamation_22
    };

    private char[] japaneses2 = {
            //とりあえず，が行の処理を考える．
            'が','ぎ','ぐ','げ','ご'
    };

    private int[] res2 = {
            //か行
            R.drawable.ka_33,R.drawable.ki_35,R.drawable.ku_41,R.drawable.ke_43,R.drawable.ko_42
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_b);
        // 言語選択 0:日本語、1:英語、2:オフライン、その他:General
        lang = 0;

        //表示
        toB_ =(EditText)findViewById(R.id.toB_);
        toB_.addTextChangedListener(this);

        voice = (Button)findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //音声認識開始
                voice();
            }
        });

        imageView = new ImageView[]{
                (ImageView)findViewById(R.id.toBI1),
                (ImageView)findViewById(R.id.toBI2),
                (ImageView)findViewById(R.id.toBI3),
                (ImageView)findViewById(R.id.toBI4),
                (ImageView)findViewById(R.id.toBI5),
                (ImageView)findViewById(R.id.toBI6),
                (ImageView)findViewById(R.id.toBI7),
                (ImageView)findViewById(R.id.toBI8),
                (ImageView)findViewById(R.id.toBI9),
                (ImageView)findViewById(R.id.toBI10),
                (ImageView)findViewById(R.id.toBI11),
                (ImageView)findViewById(R.id.toBI12),
                (ImageView)findViewById(R.id.toBI13),
                (ImageView)findViewById(R.id.toBI14),
                (ImageView)findViewById(R.id.toBI15),
                (ImageView)findViewById(R.id.toBI16)
        };

        //braillesの初期化
        brailles = new Braille[64];
        for(int i = 0;i<res.length;i++){
            Braille braille = new Braille(res[i],japaneses[i]);
            brailles[i] = braille;
        }

        //brailles2の初期化
        brailles2 = new Braille[5];
        for(int i = 0;i<res2.length;i++){
            Braille braille = new Braille(R.drawable.ten_16,res2[i],japaneses2[i]);
            brailles2[i] = braille;
        }

    }

    public void translate(View v){
        translate();
    }

    private void voice(){
        //editText(toB_)を消去
        toB_.setText("");
        // 音声認識が使えるか確認する
        try {
            // 音声認識の Intent インスタンス
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            if(lang == 0){
                // 日本語
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString() );
            }
            else if(lang == 1){
                // 英語
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString() );
            }
            else if(lang == 2){
                // Off line mode
                intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
            }
            else{
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            }

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力");
            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            toB_.setText("No Activity " );
        }
    }

    // 結果を受け取るために onActivityResult を設置
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        voice1 = "";

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 認識結果を ArrayList で取得
            ArrayList<String> candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (candidates.size() > 0) {
                // 認識結果候補で一番有力なものを表示
                voice1 = (candidates.get(0));
                System.out.println("所得した漢字は" + (candidates.get(0)));
                System.out.println("voice1は" + voice1);
                voice2 = getKatakana(voice1);
                System.out.println(getKatakana(voice2));
                voice3 = zenkakuHiraganaToZenkakuKatakana(voice2);
                voice4 = katakanaToHiragana(voice3);
                System.out.println(voice4);
                toB_.setText(voice4);


//                voice2 = getKatakana(voice1);
//                System.out.println("取得したカタカナは" + voice2);
//                toB_.setText(voice2);


//                System.out.println("取得したカタカナは"+ (getKatakana(candidates.get(0))));
//                toB_.setText(zenkakuHiraganaToZenkakuKatakana(getKatakana(candidates.get(0))));
//                textView2.setText(candidates.get(1));
//                textView3.setText(candidates.get(2));
//                for(int i = 0; i < candidates.size() ;i++){
//                    System.out.println(candidates.get(0));
            }
        }
        voice1 = "";
    }

    public static String getKatakana(String word) {
        if (Strings.isNullOrEmpty(word))
            return null;
        Tokenizer.Builder builder = new Tokenizer.Builder();
        builder.mode(TokenizerBase.Mode.NORMAL);
        Tokenizer tokenizer = builder.build();
        List<Token> tokens = tokenizer.tokenize(word);
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens)
            sb.append(token.getReading());
        return sb.toString();
    }

    public static String katakanaToHiragana(String w) {
        if (Strings.isNullOrEmpty(w))
            return null;
        Transliterator transliterator = Transliterator.getInstance("Katakana-Hiragana");
        String result = transliterator.transliterate(w);
        System.out.println(result);
        return result;
    }

    public static String zenkakuHiraganaToZenkakuKatakana(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c >= 'ァ' && c <= 'ン') {
                sb.setCharAt(i, (char)(c - 'ァ' + 'ぁ'));
            } else if (c == 'ヵ') {
                sb.setCharAt(i, 'か');
            } else if (c == 'ヶ') {
                sb.setCharAt(i, 'け');
            } else if (c == 'ヴ') {
                sb.setCharAt(i, 'う');
                sb.insert(i + 1, '゛');
                i++;
            }
        }
        return sb.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

//        EditText toB_ = (EditText) findViewById(R.id.toB_);
//        toB_.setText(s.toString());


        System.out.println("キーボードから取得中" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {

        translate();


    }

    public void translate(){
        //EditTextから文字列取得
        String str = toB_.getText().toString().trim();

        //文字列をchar型に変換
        char[] charArray = str.toCharArray();

        //Log.d("toBActivity:",str);

        //char型からint型の画像データを検索

        int[] res_data = new int[16]; //16画像の表示制限

        int index = 0;

        for(int i = 0; i < charArray.length;i++){

            int j = 0;

            //50音の処理判定
            for(j = 0; j < res.length;j++){
                if(charArray[i] == brailles[j].getC_japanese()){
                    res_data[index] = brailles[j].getRes();
                    index++;
                    break;
                }
            }

            //濁点処理判定
            if(j == res.length){
                for(int k = 0; k < res2.length ; k++){
                    if(charArray[i] == brailles2[k].getC_japanese()){
                        res_data[index] = brailles2[k].getRes1();
                        res_data[++index] = brailles2[k].getRes2();
                        index++;
                        break;
                    }
                }
            }

            if(index > 15){
                toB_.setText("申し訳ありません．文字数オーバーです．");
                break;
            }

        }

        //画像データの表示

        for(int l = 0;l < res_data.length;l++){
            imageView[l].setImageResource(res_data[l]);
        }
    }
}
