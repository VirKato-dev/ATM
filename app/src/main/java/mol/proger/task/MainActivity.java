package mol.proger.task;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Activity {

    private HashMap<String, Object> bank = new HashMap<>();
    private double cash = 0;
    private double total = 0;
    private double pos = 0;
    private String key = "";

    private ArrayList<String> errors = new ArrayList<>();
    private ArrayList<Double> incashier = new ArrayList<>();
    private ArrayList<String> banknotes = new ArrayList<>();

    private TextView textview1;
    private LinearLayout linear2;
    private Button b_get;
    private TextView t_amount;
    private TextView textview2;
    private EditText e_money;
    private TextView textview3;
    private ListView lv_errors;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        textview1 = findViewById(R.id.textview1);
        linear2 = findViewById(R.id.linear2);
        b_get = findViewById(R.id.b_get);
        t_amount = findViewById(R.id.t_amount);
        textview2 = findViewById(R.id.textview2);
        e_money = findViewById(R.id.e_money);
        textview3 = findViewById(R.id.textview3);
        lv_errors = findViewById(R.id.lv_errors);

        b_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                // НЕ ИЗМЕНЯТЬ НЕ УДАЛЯТЬ
                errors.clear();
                try {
                    cash = _getMoney(Double.parseDouble(e_money.getText().toString()));
                    if (cash < 1) {
                        errors.add("Не может быть выдано: ".concat(String.valueOf((long) (cash))));
                    } else {
                        errors.add("Выдано: ".concat(String.valueOf((long) (cash))));
                        errors.add("В банкомате осталось купюр:");
                        SketchwareUtil.getAllKeysFromMap(bank, banknotes);
                        while (banknotes.size() > 0) {
                            if (bank.containsKey(banknotes.get((int) (0)))) {
                                errors.add(banknotes.get((int) (0)).concat(" : ".concat(bank.get(banknotes.get((int) (0))).toString())));
                            }
                            banknotes.remove((int) (0));
                        }
                    }
                } catch (Exception e) {
                    errors.add("Неверный ввод запрашиваемых средств");
                }
                lv_errors.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, errors));
                ((BaseAdapter) lv_errors.getAdapter()).notifyDataSetChanged();
                lv_errors.setVisibility(View.VISIBLE);
                _showBalance();
            }
        });
    }

    private void initializeLogic() {
        // НЕ ИЗМЕНЯТЬ
        _test();
        if (true) {
            incashier = new Gson().fromJson("[1000,500,500,200,200,200,200,200,100,100,50,10]", new TypeToken<ArrayList<Double>>() {
            }.getType());
            _load();
            _showBalance();
        }
    }

    public double _getMoney(final double _money) {
        // ВАШЕ РЕШЕНИЕ В БЛОКЕ IF
        // РАЗРЕШЕНО СОЗДАВАТЬ МОРБЛОКИ
        // Map:bank - купюры в банкомате
        // Number:money - требуется выдать
        if (!(_money == 0)) {
            // здесь реализовать логику выдачи средств
            if (true) {
                // ...
                cash = 0;
                if (total < _money) {
                    return (-1);
                }
                SketchwareUtil.getAllKeysFromMap(bank, banknotes);
                incashier.clear();
                while (banknotes.size() > 0) {
                    for (int _repeat49 = 0; _repeat49 < (int) (Double.parseDouble(bank.get(banknotes.get((int) (0))).toString())); _repeat49++) {
                        incashier.add(Double.valueOf(Double.parseDouble(banknotes.get((int) (0)))));
                    }
                    banknotes.remove((int) (0));
                }
                Collections.sort(incashier);
                pos = incashier.size() - 1;
                while ((pos > -1) && (cash < _money)) {
                    if (!((_money - cash) < incashier.get((int) (pos)).doubleValue())) {
                        cash = cash + incashier.get((int) (pos)).doubleValue();
                        incashier.remove((int) (pos));
                    }
                    pos--;
                }
                if (cash == _money) {
                    _load();
                } else {
                    return (-1);
                }
            }
            // return money при успешной выдаче
            return (_money);
        }
        // если не получилось выдать
        return (-1);
    }


    public void _test() {
        // НЕ ИЗМЕНЯТЬ НЕ УДАЛЯТЬ
        errors.clear();
        errors.add("Ошибки при тестировании вашего решения:");
        incashier = new Gson().fromJson("[50]", new TypeToken<ArrayList<Double>>() {
        }.getType());
        _load();
        if (!(-1 == _getMoney(-50))) {
            errors.add("Выдана отрицательная сумма средств");
        }
        incashier = new Gson().fromJson("[50]", new TypeToken<ArrayList<Double>>() {
        }.getType());
        _load();
        if (!(-1 == _getMoney(20))) {
            errors.add("Выдана невозможная сумма средств");
        }
        incashier = new Gson().fromJson("[50]", new TypeToken<ArrayList<Double>>() {
        }.getType());
        _load();
        if (!(-1 == _getMoney(100))) {
            errors.add("Выдана сумма средств больше чем есть в банкомате");
        }
        if (-1 == _getMoney(50)) {
            errors.add("Банкомат уменьшает количество доступных средств даже при отказе");
        }
        incashier = new Gson().fromJson("[100,50]", new TypeToken<ArrayList<Double>>() {
        }.getType());
        _load();
        cash = _getMoney(50);
        if (!(-1 == _getMoney(150))) {
            errors.add("Банкомат не уменьшает количество доступных средств");
        }
        if (errors.size() > 1) {
            lv_errors.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, errors));
            ((BaseAdapter) lv_errors.getAdapter()).notifyDataSetChanged();
            lv_errors.setVisibility(View.VISIBLE);
        }
    }


    public void _load() {
        // РЕАЛИЗОВАТЬ ПОПОЛНЕНИЕ БАНКОМАТА КУПЮРАМИ
        bank = new HashMap<>();
        // ПОЛОЖИТЬ КУПЮРЫ В БАНКОМАТ ИЗ СПИСКА ListNumber:incashier
        if (true) {
            // ...
            while (incashier.size() > 0) {
                if (!bank.containsKey(String.valueOf((long) (incashier.get((int) (0)).doubleValue())))) {
                    bank.put(String.valueOf((long) (incashier.get((int) (0)).doubleValue())), "0");
                }
                cash = Double.parseDouble(bank.get(String.valueOf((long) (incashier.get((int) (0)).doubleValue()))).toString());
                cash++;
                bank.put(String.valueOf((long) (incashier.get((int) (0)).doubleValue())), String.valueOf((long) (cash)));
                incashier.remove((int) (0));
            }
        }
    }


    public void _showBalance() {
        // РЕАЛИЗОВАТЬ ПОКАЗ ОСТАТКА СРЕДСТВ В БАНКОМАТЕ
        total = 0;
        if (true) {
            // ...
            banknotes.clear();
            SketchwareUtil.getAllKeysFromMap(bank, banknotes);
            while (banknotes.size() > 0) {
                key = banknotes.get((int) (0));
                cash = Double.parseDouble(bank.get(key).toString());
                total = total + (Double.parseDouble(key) * cash);
                banknotes.remove((int) (0));
            }
        }
        t_amount.setText(String.valueOf((long) (total)));
    }


    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
