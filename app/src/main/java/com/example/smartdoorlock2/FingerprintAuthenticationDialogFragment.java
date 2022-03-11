/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.smartdoorlock2;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class FingerprintAuthenticationDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener, FingerprintUiHelper.Callback {

    private Button mCancelButton;
    private Button mSecondDialogButton;
    private View mFingerprintContent;
    private View mBackupContent;
    private EditText mPassword;
    private CheckBox mUseFingerprintFutureCheckBox;
    private TextView mPasswordDescriptionTextView;
    private TextView mNewFingerprintEnrolledTextView;

    private FingerprintManagerCompat.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private MainActivity mActivity;

    private InputMethodManager mInputMethodManager;

    private SecretAuthorize secretAuthorize;
    private int mStage = Constant.FINGERPRINT;
    private Animation vibrateAnim;
    private SharedPreferences mSharedPreferences;

    public FingerprintAuthenticationDialogFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (Build.VERSION.SDK_INT > LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
        } else {

        }
        vibrateAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.vibrate_anim); // 틀렸을 때 흔들리는 애니메이션 추가
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); // 저장한 비밀번호를 받아오기 위한 쉐어드프리퍼런스 생성
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.sign_in)); // 다이얼로그의 제목 설정
        View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false); //inflater 를 이용한 xml 뷰 생성
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mSecondDialogButton = (Button) v.findViewById(R.id.second_dialog_button);
        mSecondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Constant.FINGERPRINT) {
                    goToBackup();
                } else {
                    verifyPassword();
                }
            }
        });
        mFingerprintContent = v.findViewById(R.id.fingerprint_container);
        mBackupContent = v.findViewById(R.id.backup_container);
        mPassword = (EditText) v.findViewById(R.id.password);
        mPassword.setOnEditorActionListener(this);
        mPasswordDescriptionTextView = (TextView) v.findViewById(R.id.password_description);
        mUseFingerprintFutureCheckBox = (CheckBox)
                v.findViewById(R.id.use_fingerprint_in_future_check);
        mNewFingerprintEnrolledTextView = (TextView)
                v.findViewById(R.id.new_fingerprint_enrolled_description);

        mFingerprintUiHelper = new FingerprintUiHelper(getActivity().getApplicationContext(),
                (ImageView) v.findViewById(R.id.fingerprint_icon),
                (TextView) v.findViewById(R.id.fingerprint_status), this);
        updateStage();

        if (!Util.isFingerprintAuthAvailable(getActivity().getApplicationContext())) {
            goToBackup();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStage == Constant.FINGERPRINT) {
            mFingerprintUiHelper.startListening(mCryptoObject);
        }
    }


    @Override

    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }


    public void setCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    private void goToBackup() {
        mStage = Constant.PASSWORD;
        updateStage();
        mPassword.requestFocus();


        mFingerprintUiHelper.stopListening();
    }

    private void verifyPassword() {

        if(mPassword.getText().toString().equals(mSharedPreferences.getString(Constant.PASSWORD_NUN ,"wrong" ))){ // 가져온 쉐어드프리퍼런스에서 저장한 비밀번호 가져오기
            dismiss();
            secretAuthorize.success();
            mStage = Constant.FINGERPRINT;
        }else{
            mBackupContent.startAnimation(vibrateAnim);
            mPassword.setText("");
        }
    }

    private boolean checkPassword(String password) {
        return password.length() > 0;
    }


    private void updateStage() {
        switch (mStage) {
            case Constant.FINGERPRINT:
                mCancelButton.setText(R.string.cancel);
                mSecondDialogButton.setText(R.string.use_password);
                mFingerprintContent.setVisibility(View.VISIBLE);
                mBackupContent.setVisibility(View.GONE);
                break;
            case Constant.PASSWORD:
                mCancelButton.setText(R.string.cancel);
                mSecondDialogButton.setText(R.string.ok);
                mFingerprintContent.setVisibility(View.GONE);
                mBackupContent.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword();
            return true;
        }
        return false;
    }

    @Override
    public void onAuthenticated() {

        dismiss();
        secretAuthorize.success();
    }

    @Override
    public void onError() {
        goToBackup();
    }


    public void setCallback(SecretAuthorize secretAuthorize){
        this.secretAuthorize = secretAuthorize;
    }

    public interface SecretAuthorize {
        void success();
        void fail();
    }
}
