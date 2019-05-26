package com.KimHyunWoo.bingo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class setMatrix {

    private final String TAG = "Bingo";
    private final int max = 51; //0~50
    int [][] nMatrix = new int[5][5];

    /* 마킹 갯수*/
    int dia1=0, dia2=0; //dia1: 오른쪽상단 to 왼쪽하단 대각선, dia2: 왼쪽상단 to 오른쪽하단 대각선
    int[] wid = new int[5];
    int[] hei = new int[5];
    int count=0;

    private final int X = -1;
    protected int computerClickNumber=-1;

    setMatrix(){
        int num;
        Random r = new Random();

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                num = r.nextInt(max);
                if(check(nMatrix, num)){
                    nMatrix[i][j]=num;
                }
                else{ j--;}
            }
        }
    }

    protected boolean check(int nMatrix[][], int checkNum){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(nMatrix[i][j]==checkNum){
                    return false;
                }
            }
        }
        return true;
    }

    public int getComputerClickNumber(){
        return this.computerClickNumber;
    }

    protected void computerTurn(){

        searchMark();
        findDirection();

    }

    private void searchMark(){
        //가운데 우선//
        if(nMatrix[2][2] != X){
            nMatrix[2][2] = computerClickNumber;
            nMatrix[2][2] = X;
            Log.d(TAG,"com: " + computerClickNumber);
        }else{
            for(int i =0; i<5; i++) {
                if (nMatrix[i][i] == X) dia1++;
            }

            for(int i = 4; i>=0; i--){
                if(nMatrix[4-i][i] == X) dia2++;
            }

            for(int i = 0; i<5; i++){
                count = 0;
                for(int j=0; j<5; j++){
                    if(nMatrix[i][j] == X) count++;
                }
                wid[i]=count;
            }

            for(int i = 0; i<5; i++){
                count = 0;
                for(int j=0; j<5; j++){
                    if(nMatrix[j][i] == X) count++;
                }
                hei[i]=count;
            }
        }
    }

    private void findDirection(){
        int index=0;
        ArrayList<Integer> arr= new ArrayList<>();

        //5일경우 빙고 완성이므로 제외
        if(dia1 == 5) dia1 = -2;
        if(dia2 == 5) dia2 = -2;
        arr.add(dia1);
        arr.add(dia2);
        for(Integer temp : hei){
            if(temp == 5) temp = -2;
            arr.add(temp);
        }
        for(Integer temp : wid){
            if(temp == 5) temp = -2;
            arr.add(temp);
        }

        //max: 구역 중 제일 큰 값
        //max값을 가진 구역을 찾는다. 1순위. 대각선, 2순위. 가로, 3순위. 세로

        while(index <5){
            if(dia1 == max){
                findPosition("dia1", index);
                Log.d(TAG,"com: Target-dia1");
                break;
            }else if(dia2 == max){
                findPosition("dia2", index);
                Log.d(TAG,"com: Target-dia2");
                break;
            }else if(wid[index] == max){
                findPosition("wid", index);
                Log.d(TAG,"com: Target-wid");
                break;
            }else if(hei[index] == max){
                findPosition("hei", index);
                Log.d(TAG,"com: Target-hei");
                break;
            }else{
                index++;
            }
        }
    }

    //정해진 구역에서 어디에 마킹할지 찾는다.
    private void findPosition(String target, int index){
        ArrayList<Integer> arr= new ArrayList<>();
        //int[] arr = new int[5];
        int mark=-1;
        switch(target){
            case "dia1":
                for(int i=0; i<5; i++){
                    if(nMatrix[i][i] != X){
                        arr.add(checkCross(i,i));
                    }else{
                        arr.add(0);
                    }
                }
                marking(arr,target, index);
                break;

            case "dia2":    //arr: [4][0] / [3][1] / [2][2] / [1][3] / [0][4] 순서
                for(int i=4; i>=0; i--){
                    if(nMatrix[4-i][i] != X){
                        arr.add(checkCross(4-i,i));
                    }else{
                        arr.add(0);
                    }
                }
                marking(arr,target, index);
                break;

            case "wid":
                for(int i=0; i<5; i++){
                    if(nMatrix[index][i] != X){
                        arr.add(checkCross(index,i));
                    }else{
                        arr.add(0);
                    }
                }
                marking(arr,target,index);
                break;

            case "hei":
                for(int i=0; i<5; i++){
                    if(nMatrix[i][index]!=X){
                        arr.add(checkCross(i,index));
                    }else{
                        arr.add(0);
                    }
                }
                marking(arr,target,index);
                break;

        }
    }

    //각 구역 5개 요소의 가로 세로에 있는 x 갯수를 비교하여 많은 위치에 마킹
    private int  checkCross(int wid, int hei){
        int n_wid=0, n_hei=0;

        //가로//
        for(int i =0; i<5; i++){
            if(nMatrix[wid][i]==X) n_wid++;
        }

        //세로
        for(int i  =0; i<5; i++){
            if(nMatrix[i][hei] == X) n_hei++;
        }

        return n_wid + n_hei;
    }


    //target_index: 각 타겟의 인덱스 => 타겟: 대각선, 가로, 세로
    //즉, 타겟이 가로이고 target_index = 1이라면 nMatrix[target_index][N] 이 된다. => 가로 target_index번째 줄
    private void marking(ArrayList<Integer> arr, String target, int target_index){
        int max = Collections.max(arr);

        int index = arr.indexOf(max);

        switch(target){
            case "dia1":
                if(nMatrix[index][index]==X){
                    for(int i=index+1; i<5; i++){
                        if(max>arr.get(i)){
                            max=arr.get(i);
                        }
                    }
                    computerClickNumber = nMatrix[arr.indexOf(max)][arr.indexOf(max)];
                    nMatrix[arr.indexOf(max)][arr.indexOf(max)] = -1;
                }else{
                    computerClickNumber = nMatrix[index][index];
                    nMatrix[index][index] = -1;
                }
                break;
            case "dia2":
                if(nMatrix[4-index][index]==X){
                    for(int i=index+1; i<5; i++){
                        if(max>arr.get(i)){
                            max=arr.get(i);
                        }
                    }
                    computerClickNumber = nMatrix[4-arr.indexOf(max)][arr.indexOf(max)];
                    nMatrix[4-arr.indexOf(max)][arr.indexOf(max)] = -1;
                }else{
                    computerClickNumber = nMatrix[4-index][index];
                    nMatrix[4-index][index] = -1;
                }
                break;

            case "wid":
                if(nMatrix[target_index][index]==X){
                    for(int i= index+1; i<5; i++){
                        if(max>arr.get(i)){
                            max = arr.get(i);
                        }
                    }
                    computerClickNumber = nMatrix[target_index][arr.indexOf(max)];
                    nMatrix[target_index][arr.indexOf(max)] = -1;
                }else{
                    computerClickNumber = nMatrix[target_index][index];
                    nMatrix[target_index][index] = -1;
                }
                break;

            case "hei":
                if(nMatrix[index][target_index] == X){
                    for(int i=index+1; i<5; i++){
                        if(max>arr.get(i)){
                            max = arr.get(i);
                        }
                    }
                    computerClickNumber = nMatrix[arr.indexOf(max)][target_index];
                    nMatrix[arr.indexOf(max)][target_index] = -1;

                }else{
                    computerClickNumber = nMatrix[index][target_index];
                    nMatrix[index][target_index] = -1;
                }
        }
    }
}
