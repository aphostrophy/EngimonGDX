package com.ungabunga.model.entities;

//import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
//import javafx.util.*;
//import com.ungabunga.model.entities.Engimon;
//import com.ungabunga.model.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ungabunga.model.enums.IElements;
import com.ungabunga.model.utilities.Pair;

public class Battle {
    private static final int arrSize = 5;
    private static final int FIRE_INDEX =  0;
    private static final int WATER_INDEX = 1;
    private static final int ELECTRIC_INDEX =  2;
    private static final int GROUND_INDEX = 3;
    private static final int ICE_INDEX = 4;

    private float powerPlayer,powerEnemy;
    private float[][] advantageChart = new float[arrSize][arrSize];
    private  Engimon playerEngimons, enemyEngimons;

    public Battle() {
        this.generateChart();
    }

    //setter and getter
    public void HitungPower(Engimon engimon1, Engimon engimon2)
    {
        int sumPowerPlayer = 0, sumPowerEnemy = 0;
        for (int i = 0; i < engimon1.getSkills().size(); i++)
        {
            sumPowerPlayer += engimon1.getSkills().get(i).getBasePower();
        }
        for (int i = 0; i < engimon2.getSkills().size(); i++)
        {
            sumPowerEnemy += engimon2.getSkills().get(i).getBasePower();
        }
        Pair<Float, Float> PairElementAdvantage = new Pair<Float, Float>(this.elementAdvantage());
        this.powerPlayer = (engimon1.getLevel() * PairElementAdvantage.getFirst()) + sumPowerPlayer;
        this.powerEnemy = (engimon2.getLevel() * PairElementAdvantage.getSecond()) + sumPowerEnemy;
    }

    //Method
    public String showTotalPower()
    {
        String str = ("Power level engimon player : " + this.powerPlayer +  "\n" + "Power level engimon enemy : " + this.powerEnemy + "\n");
        return str;
    }

    private Boolean isPlayerLose()
    {
        if (this.powerPlayer <= this.powerEnemy)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void generateChart()
    {
        int i = 0, j = 0;
        FileHandle inputFile = Gdx.files.internal("resources/advantageChart.csv");
//            BufferedReader inputFile = new BufferedReader(new FileReader("../constants/advantageChart.csv"));
//            String line;
        String text = inputFile.readString();
        String linesArray[] = text.split("\\r?\\n");
        i = 0;
        for (String line : linesArray) {
            String words[] = line.split(",");
            j = 0;
            for (String str : words) {
                float str_float = Float.parseFloat(str);
                this.advantageChart[i][j] = str_float;
                j++;
            }
            i++;
        }

    }

    private Pair<Float, Float> elementAdvantage()
    {
        float elAdvantagePlayer = 0, elAdvantageEnemy = 0;
        int elPlayerSize = this.playerEngimons.getElements().size();
        int elEnemySize = this.enemyEngimons.getElements().size();
        Pair<Integer,Integer> indexElPlayer, indexElEnemy;
        indexElPlayer = checkType(true);
        indexElEnemy = checkType(false);

        for (int j = 0; j < elPlayerSize; j++)
        {
            for (int k = 0; k < elEnemySize; k++)
            {
                float x, y;
                if (j == 0 && k == 0)
                {
                    x = advantageChart[indexElPlayer.getFirst()][indexElEnemy.getFirst()];
                    y = advantageChart[indexElEnemy.getFirst()][indexElPlayer.getFirst()];
                    elAdvantagePlayer = Math.max(x, elAdvantagePlayer);
                    elAdvantageEnemy = Math.max(y, elAdvantageEnemy);
                }
                else if (j == 0 && k == 1)
                {
                    x = advantageChart[indexElPlayer.getFirst()][indexElEnemy.getSecond()];
                    y = advantageChart[indexElEnemy.getSecond()][indexElPlayer.getFirst()];
                    elAdvantagePlayer = Math.max(x, elAdvantagePlayer);
                    elAdvantageEnemy = Math.max(y, elAdvantageEnemy);
                }
                else if (j == 1 && k == 0)
                {
                    x = advantageChart[indexElPlayer.getSecond()][indexElEnemy.getFirst()];
                    y = advantageChart[indexElEnemy.getFirst()][indexElPlayer.getSecond()];
                    elAdvantagePlayer = Math.max(x, elAdvantagePlayer);
                    elAdvantageEnemy = Math.max(y, elAdvantageEnemy);
                }
                else if (j == 1 && k == 1)
                {
                    x = advantageChart[indexElPlayer.getSecond()][indexElEnemy.getSecond()];
                    y = advantageChart[indexElEnemy.getSecond()][indexElPlayer.getSecond()];
                    elAdvantagePlayer = Math.max(x, elAdvantagePlayer);
                    elAdvantageEnemy = Math.max(y, elAdvantageEnemy);
                }
            }
        }
        Pair<Float, Float> PairElementAdvantage = new Pair<>(elAdvantagePlayer,elAdvantageEnemy);
        return PairElementAdvantage;
    }

    private Pair<Integer, Integer> checkType(Boolean isPlayer)
    {
        List<IElements> el;
        if (isPlayer) {
            el = this.playerEngimons.getElements();
        }
        else {
            el = this.enemyEngimons.getElements();
        }
        Pair<Integer, Integer> elPair = new Pair<>(-1,-1);
        for (int i = 0; i < el.size(); i++)
        {
            if (el.get(i) == IElements.FIRE)
            {
                if (i == 0)
                {
                    elPair.setFirst(FIRE_INDEX);
                }
                else
                {
                    elPair.setSecond(FIRE_INDEX);
                }
            }
            else if (el.get(i) == IElements.WATER)
            {
                if (i == 0)
                {
                    elPair.setFirst(WATER_INDEX);
                }
                else
                {
                    elPair.setSecond(WATER_INDEX);
                }
            }
            else if (el.get(i) == IElements.ELECTRIC)
            {
                if (i == 0)
                {
                    elPair.setFirst(ELECTRIC_INDEX);
                }
                else
                {
                    elPair.setSecond(ELECTRIC_INDEX);
                }
            }
            else if (el.get(i) == IElements.GROUND)
            {
                if (i == 0)
                {
                    elPair.setFirst(GROUND_INDEX);
                }
                else
                {
                    elPair.setSecond(GROUND_INDEX);
                }
            }
            else if (el.get(i) == IElements.ICE)
            {
                if (i == 0)
                {
                    elPair.setFirst(ICE_INDEX);
                }
                else
                {
                    elPair.setSecond(ICE_INDEX);
                }
            }
        }
        return elPair;
    }

    public Boolean BattleStatusIsWin()
    {
        if (this.isPlayerLose()) {
            return false;
            // Lanjut Command Selanjutnya
        } else // player menang
        {
            // ini ntar buat di gamestate
            return true;
            //  item yang kompatibel dengan lawan
        }
    }
    public void BattleEngimon(Engimon engimon1, Engimon engimon2)
    {
        this.playerEngimons = engimon1;
        this.enemyEngimons = engimon2;
        this.HitungPower(engimon1, engimon2);
    }
}
// cd src/com/ungabunga/model/entities