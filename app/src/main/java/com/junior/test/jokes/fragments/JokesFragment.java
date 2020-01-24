package com.junior.test.jokes.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.junior.test.jokes.R;
import com.junior.test.jokes.jokes.JokeEvent;
import com.junior.test.jokes.jokes.JokesAdapter;
import com.junior.test.jokes.jokes.JokesModel;

import org.greenrobot.eventbus.EventBus;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JokesFragment extends Fragment {

    private static final String URL = "http://api.icndb.com/jokes/random";


    private RestTemplate template = new RestTemplate(true);

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private Button mButton;
    private ImageButton btnMius, btnPlus;
    private EditText countOfJokes;

    private ArrayList<String> jokesList;

    public JokesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        jokesList = new ArrayList<>();
        jokesList.add(0, "test");
        mAdapter = new JokesAdapter(jokesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jokes, container, false);

        mRecyclerView = view.findViewById(R.id.list_of_jokes);

        mButton = view.findViewById(R.id.btn_getjoke);

        btnMius = view.findViewById(R.id.btn_minus);
        btnPlus = view.findViewById(R.id.btn_plus);

        countOfJokes = view.findViewById(R.id.count_joke);
        //countOfJokes.setText(0);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_getJoke();
        initRecyclerView();
        btnMinus();
        btnPlus();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private String addJoke() {
        String jsonTxt = template.getForObject(URL, String.class);
        Log.d("jokesfromICNDB", jsonTxt);
        JokesModel jokes = new Gson().fromJson(jsonTxt, JokesModel.class);
        return jokes.getJoke();
    }

    private void btnMinus() {
        btnMius.setOnClickListener(v -> {
            countOfJokes.setText(Integer.parseInt(countOfJokes.getText().toString()) + 1);
        });
    }

    private void btnPlus() {
        btnPlus.setOnClickListener(v -> {
            countOfJokes.setText(Integer.parseInt(countOfJokes.getText().toString()) + 1);
        });
    }

    private void btn_getJoke() {
        mButton.setOnClickListener(v -> {

            //int n = Integer.parseInt(countOfJokes.getText().toString());
            int n = 5;

            Observable.create((ObservableOnSubscribe<String>) e -> {
                try {
                    jokesList.clear();
                    for (int i = 0; i < n; i++) {
                        jokesList.add(i, addJoke());
                        EventBus.getDefault().post(new JokeEvent("myEvent"));
                    }
                } catch (Exception ex) {
                    e.onError(ex);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(match -> Log.i("ASIN", "Susses" + jokesList.get(1)),
                            throwable -> Log.e("ASIN", "Error: " + throwable.getMessage()));
        });
    }

    public void updateRecyclerView() {
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

}


//TODO: Анимация, поле ввода и кнопка , скрытие боттом нава, обработка кнопок, сохранение состояния