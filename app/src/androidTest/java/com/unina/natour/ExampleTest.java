package com.unina.natour;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleTest {
    //TODO inserie la classe da testare
    //Classe classe

    @Before
    public void setUp(){
        //init classe
    }

    @Test
    public void test1(){
        //funzione classe

        //forza FAIL
        //fail();
    }

    //ci si aspetta che la funzione lanci un eccezione unchecked
    @Test(expected = IllegalArgumentException.class)
    public void test2(){
        //funzione classe
    }

    //ci si aspetta che la funzione lanci un eccezione checked
    @Test
    public void test3(){
        //funzione classe

        try{
            //funzione
        }
        catch(Exception e){

        }
    }



}
