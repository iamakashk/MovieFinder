package com.akash.android.moviefinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button mSearchButton;
    EditText mSearchText;
    String mCompleteURLRequest;
    String mEnteredText;
    private static final String OMDB_REQUEST_URL =
            "http://www.omdbapi.com/?t=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchButton = (Button) findViewById(R.id.SearchButton);
        mSearchText =(EditText) findViewById(R.id.SearchEditText);
    }
    private void updateUi(MovieDetails result) {
   TextView TitleText = (TextView) findViewById(R.id.MovieTitleText);
   TextView GenreText =(TextView) findViewById(R.id.MovieGenreText);
        TextView ReleasedText = (TextView) findViewById(R.id.MovieReleaseDateText);
        TextView PlotText = (TextView) findViewById(R.id.MoviePlotText);
        TextView ImdbRating =(TextView) findViewById(R.id.MovieRatingText);
        ImageView MovieImage = (ImageView) findViewById(R.id.MovieImage);
        TitleText.setText(result.Title);
        GenreText.setText("Genre: \n" + result.Genre + "\n");
        ReleasedText.setText("Release Date: \n" + result.Released+ "\n");
        PlotText.setText("Plot:\n" + result.Plot);
        ImdbRating.setText("IMDB Rating: \n" +result.Rated);
        Picasso.with(this).load(result.Image).into(MovieImage);
 }

        public void OnSearchClick(View view){

            if(isConnectedToInternet())
            {
                mEnteredText = mSearchText.getText().toString();
                String restUrl="";
                try {
                    restUrl = URLEncoder.encode(mEnteredText, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                mCompleteURLRequest = OMDB_REQUEST_URL + restUrl;

                Log.v("name" , restUrl);
                MovieAsyncTask movieAsyncTask = new MovieAsyncTask();
                movieAsyncTask.execute(mCompleteURLRequest);
            }
            else
            {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                 }
        }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    public final void updateUiIfNull(MovieDetails result) {
        if(result==null){
            Log.v("result","result null");
            TextView TitleText = (TextView) findViewById(R.id.MovieTitleText);
            TextView GenreText =(TextView) findViewById(R.id.MovieGenreText);
            TextView ReleasedText = (TextView) findViewById(R.id.MovieReleaseDateText);
            TextView PlotText = (TextView) findViewById(R.id.MoviePlotText);
            TextView ImdbRating =(TextView) findViewById(R.id.MovieRatingText);
            ImageView MovieImage = (ImageView) findViewById(R.id.MovieImage);
            TitleText.setText("");
            GenreText.setText("");
            ReleasedText.setText("");
            PlotText.setText("No Result Found");
            ImdbRating.setText("");
            Picasso.with(this).load("http://cdn.tumbltrak.com/media/250x250/blank.png?3.2420").into(MovieImage);
        }
    }
     private class MovieAsyncTask extends AsyncTask<String, Void, MovieDetails> {
         private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
         protected void onPreExecute() {
             this.dialog.setMessage("Please wait");
             this.dialog.show();
         }

        protected MovieDetails doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

           MovieDetails result = Utils.fetchMovieData(urls[0]);

            return  result;
        }

        /**
         * This method is invoked on the main UI thread after the background work has been
         * completed.
         */
        protected void onPostExecute(MovieDetails result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result == null) {
                updateUiIfNull(result);
            } else {
                updateUi(result);
            }
        }
    }
}
