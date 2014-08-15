package com.lemox.game2048;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{

	public MainActivity()
	{
		mainActivity = this;
	}

	@SuppressLint("WorldReadableFiles")
	public void saveGame() throws IOException
	{
		// TODO save
		@SuppressWarnings("deprecation")
		FileOutputStream outputStream = this.openFileOutput("gamedata.txt",
				Context.MODE_WORLD_READABLE);

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				int tmp = gameView.cardsMap[i][j].getNum();
				outputStream.write(Integer.toString(tmp).getBytes());
				outputStream.write(" ".getBytes());
			}
			outputStream.write("\n".getBytes());
		}

		outputStream.write(Integer.toString(score).getBytes());
		outputStream.write("\n".getBytes());

		outputStream.close();

		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("´æµµ")
				.setMessage("´æµµÍê³É!").setIcon(R.drawable.ic_launcher).create();
		alertDialog.show();

	}

	@SuppressLint("UseValueOf")
	public void loadGame() throws IOException
	{
		// TODO load
		FileInputStream inputStream = this.openFileInput("gamedata.txt");
		Scanner scanner = new Scanner(inputStream);
		byte[] buffer = new byte[1024];

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				int tmp;
				tmp = scanner.nextInt();
				gameView.cardsMap[i][j].setNum(tmp);
			}
		}
		score = scanner.nextInt();
		showScore();
		inputStream.close();

		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("¶Áµµ")
				.setMessage("¶ÁµµÍê³É!").setIcon(R.drawable.ic_launcher).create();
		alertDialog.show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		root = (LinearLayout) findViewById(R.id.container);
		root.setBackgroundColor(0xfffaf8ef);

		tvScore = (TextView) findViewById(R.id.tvScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);

		gameView = (GameView) findViewById(R.id.gameView);

		btnNewGame = (Button) findViewById(R.id.btnNewGame);
		btnNewGame.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				gameView.startGame();
			}
		});

		btnLoadGame = (Button) findViewById(R.id.btnLoadGame);
		btnLoadGame.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					loadGame();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnSaveGame = (Button) findViewById(R.id.btnSaveGame);
		btnSaveGame.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					// gameView.saveGame();
					saveGame();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		animLayer = (AnimLayer) findViewById(R.id.animLayer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clearScore()
	{
		score = 0;
		showScore();
	}

	public void showScore()
	{
		tvScore.setText(score + "");
	}

	public void addScore(int s)
	{
		score += s;
		showScore();

		int maxScore = Math.max(score, getBestScore());
		saveBestScore(maxScore);
		showBestScore(maxScore);
	}

	public void saveBestScore(int s)
	{
		Editor e = getPreferences(MODE_PRIVATE).edit();
		e.putInt(SP_KEY_BEST_SCORE, s);
		e.commit();
	}

	public int getBestScore()
	{
		return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
	}

	public void showBestScore(int s)
	{
		tvBestScore.setText(s + "");
	}

	public AnimLayer getAnimLayer()
	{
		return animLayer;
	}

	private int score = 0;
	private TextView tvScore, tvBestScore;
	private LinearLayout root = null;
	private Button btnNewGame;
	private Button btnSaveGame;
	private Button btnLoadGame;

	private GameView gameView;
	private AnimLayer animLayer = null;

	private static MainActivity mainActivity = null;

	public static MainActivity getMainActivity()
	{
		return mainActivity;
	}

	public static final String SP_KEY_BEST_SCORE = "bestScore";

}

