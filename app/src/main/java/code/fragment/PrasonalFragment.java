package code.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.zozima.android.R;

import java.util.Calendar;

import code.database.AppSettings;
import code.view.BaseFragment;
import code.view.CustomCheckBox;


public class PrasonalFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match

    // TextInputEditText
    TextInputEditText edt_dob, edt_gender, edt_language, edt_material, edt_morethenone;

    //TextView
    TextView tvOk;


    public PrasonalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prasonal, container, false);

        findViewById(view);
        setListeners();

        if (!AppSettings.getString(AppSettings.profiledob).equals(""))
            edt_dob.setText(AppSettings.getString(AppSettings.profiledob));
        if (!AppSettings.getString(AppSettings.profilegender).equals(""))
            edt_gender.setText(AppSettings.getString(AppSettings.profilegender));
       /* if (!AppSettings.getString(AppSettings.profilelanguage).equals(""))
            edt_language.setText(AppSettings.getString(AppSettings.profilelanguage));
*/
        if (!AppSettings.getString(AppSettings.profilemarital).equals(""))
            edt_material.setText(AppSettings.getString(AppSettings.profilemarital));

        if (!AppSettings.getString(AppSettings.profilenumberofkide).equals(""))
            edt_morethenone.setText(AppSettings.getString(AppSettings.profilenumberofkide));


        edt_dob.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.profiledob, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_gender.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.profilegender, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_material.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.profilemarital, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_morethenone.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.profilenumberofkide, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void setListeners() {

        edt_dob.setOnClickListener(this);
        edt_gender.setOnClickListener(this);
        /*edt_language.setOnClickListener(this);*/
        edt_material.setOnClickListener(this);
        edt_morethenone.setOnClickListener(this);

    }


    private void findViewById(final View view) {
        edt_dob = view.findViewById(R.id.edt_dob);
        edt_gender = view.findViewById(R.id.edt_gender);
        /*edt_language = view.findViewById(R.id.edt_language);*/
        tvOk = view.findViewById(R.id.ok);
        edt_material = view.findViewById(R.id.edt_material);
        edt_morethenone = view.findViewById(R.id.edt_morethentwo);


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.male:

                edt_gender.setText(getString(R.string.male));

                return true;
            case R.id.female:
                edt_gender.setText(getString(R.string.female));

                return true;
            case R.id.other:
                edt_gender.setText(getString(R.string.otherssharednow));

                return true;
            case R.id.married:

                edt_material.setText(getString(R.string.Married));

                return true;
            case R.id.single:
                edt_material.setText(getString(R.string.Unmarried));

                return true;
            case R.id.divorced:
                edt_material.setText(getString(R.string.Divorced));

                return true;


            case R.id.none:
                edt_morethenone.setText(getString(R.string.none));
                return true;
            case R.id.one:

                edt_morethenone.setText(getString(R.string.One));
                return true;
            case R.id.two:
                edt_morethenone.setText(getString(R.string.Two));
                return true;
            case R.id.morethantwo:
                edt_morethenone.setText(getString(R.string.MoreThen));
                return true;


        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.edt_dob:

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog( getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String Selectdate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edt_dob.setText( Selectdate );
                    }
                }, calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ) );
                datePickerDialog.show();

                break;

            case R.id.edt_gender:

                PopupMenu popupMenu = new PopupMenu( getActivity(), v );
                popupMenu.setOnMenuItemClickListener( PrasonalFragment.this );
                popupMenu.inflate( R.menu.genderpomenu );

                popupMenu.show();
                break;


            case R.id.edt_material:
                PopupMenu popupMenu1 = new PopupMenu(getActivity(), v);
                popupMenu1.setOnMenuItemClickListener(PrasonalFragment.this);
                popupMenu1.inflate(R.menu.maerital_status_menu);

                popupMenu1.show();

                break;

            case R.id.edt_morethentwo:

                PopupMenu popupMenu2 = new PopupMenu(getActivity(), v);
                popupMenu2.setOnMenuItemClickListener(PrasonalFragment.this);
                popupMenu2.inflate(R.menu.more_than_two);
                popupMenu2.show();

                break;

          /*  case R.id.edt_language:

                *//*languagepop();*//*

                break;*/
        }





    }
    public  void  languagepop()
    {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.language);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        CustomCheckBox english=dialog.findViewById(R.id.english);
        CustomCheckBox hindi=dialog.findViewById(R.id.hindi);
        CustomCheckBox bengali=dialog.findViewById(R.id.bengali);
        CustomCheckBox telugu=dialog.findViewById(R.id.telugu);
        CustomCheckBox malayalam=dialog.findViewById(R.id.malayalam);
        CustomCheckBox kanada=dialog.findViewById(R.id.kanada);
        TextView tvok=dialog.findViewById(R.id.ok);




    }

}