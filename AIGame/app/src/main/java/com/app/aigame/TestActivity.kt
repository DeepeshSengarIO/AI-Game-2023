package com.app.aigame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_computer.btnFinishMove2
import kotlinx.android.synthetic.main.activity_computer.btnPlayAgain
import kotlinx.android.synthetic.main.activity_computer.currentPlayer2
import kotlinx.android.synthetic.main.activity_computer.ll11
import kotlinx.android.synthetic.main.activity_computer.ll22
import kotlinx.android.synthetic.main.activity_computer.ll33
import kotlinx.android.synthetic.main.activity_computer.ll44
import kotlinx.android.synthetic.main.activity_computer.progressBar

class TestActivity : AppCompatActivity() {
    lateinit var arr: IntArray
    var winner: Boolean = false
    private lateinit var CP: String
    var computerLost: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        //Initialize Current Player
        CP = intent.getStringExtra("cp").toString()

        init()

        btnPlayAgain!!.setOnClickListener {
            init()
            currentPlayer2.text = CP
            btnPlayAgain!!.visibility = View.GONE
            btnFinishMove2!!.visibility = View.VISIBLE
        }

    }

    fun init(){
        //Initialize Array
        arr = intArrayOf(5,3,4,7)
        //Set The Image Gravity to Center Horizontal
        setLayoutGravity()
        //Draw`s the circles
        draw()
        //Set All the Click Listeners
        setClickListeners()
        //Finish Game Button
        if (CP=="Computer"){
            updateCurrentPlayer()
            callComputer()
            callPlayer()
        }
        if (CP=="Player"){
            callPlayer()
        }

       StateSpaceGraph(arr)
    }

    fun callPlayer(){
        btnFinishMove2.setOnClickListener {
            removeViews()
            draw()
            setClickListeners()
            updateCurrentPlayer()
            checkWinner()
            if (CP=="Computer"){
                callComputer()
            }
        }
    }

    fun callComputer(){
        if(!winner){
            progressBar?.visibility = View.VISIBLE
        }
        btnFinishMove2?.visibility = View.GONE
        Handler().postDelayed({
            checkGamePlayerByComputer()
            progressBar?.visibility = View.GONE
            if (!winner){
                btnFinishMove2?.visibility = View.VISIBLE
            }
        }, 2000)
    }

    fun checkGamePlayerByComputer(){

        playComputerFunction()
        removeViews()
        draw()
        setClickListeners()
        updateCurrentPlayer()
        checkWinner()

    }

    fun setClickListeners(){

        for (i in arr.indices){
            for (j in 0 until arr[i]){
                if (i==0){
                    onClick(ll11, j)
                }else if (i==1){
                    onClick(ll22, j)
                }else if (i==2){
                    onClick(ll33, j)
                }else if (i==3){
                    onClick(ll44, j)
                }
            }
        }
    }

    fun onClick(ll: LinearLayout, idx: Int){
        ll.getChildAt(idx).setOnClickListener {
            ll.getChildAt(idx).setBackgroundResource(R.drawable.empty_circle)
            when(ll){
                ll11->{
                    var x = arr[0]
                    arr[0] = x-1
                }
                ll22->{
                    var x = arr[1]
                    arr[1] = x-1
                }
                ll33->{
                    var x = arr[2]
                    arr[2] = x-1
                }
                ll44->{
                    var x = arr[3]
                    arr[3] = x-1
                }
            }
        }
    }

    fun updateCurrentPlayer(){
        if (currentPlayer2.text == "Player"){
            currentPlayer2.text = "Computer"
            CP = "Computer"
        }else if(currentPlayer2.text == "Computer"){
            currentPlayer2.text = "Player"
            CP = "Player"
        }
    }

    fun playComputerFunction(){
        arr = computerMove()
        draw()
    }

    fun computerMove(): IntArray {
        data class Move(val index: Int, val stones: Int)
        fun minimax(arr: IntArray, depth: Int, maximizingPlayer: Boolean): Pair<Int, Move> {
            if (depth == 0 || arr.all { it == 0 }) {
                val score = if (maximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE
                return Pair(score, Move(-1, -1))
            }
            var bestMove = Move(-1, -1)
            var bestValue = if (maximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE
            for (i in arr.indices) {
                for (j in 1..arr[i]) {
                    val newArr = arr.copyOf()
                    newArr[i] -= j
                    val currentValue = minimax(newArr, depth - 1, !maximizingPlayer).first
                    if (maximizingPlayer && currentValue > bestValue) {
                        bestValue = currentValue
                        bestMove = Move(i, j)
                    } else if (!maximizingPlayer && currentValue < bestValue) {
                        bestValue = currentValue
                        bestMove = Move(i, j)
                    }
                }
            }
            return Pair(bestValue, bestMove)
        }
        val depth = 5
        val bestMove = minimax(arr, depth, true).second
        if (bestMove.index >= 0) {
            val resultArray = arr.copyOf()
            resultArray[bestMove.index] -= bestMove.stones
            return resultArray
        } else {
            Log.e("COMPUTER MOVE", "No valid move, LOST GAME")
            computerLost = true
            checkWinner()
            return arr
        }
    }

    fun removeViews(){
        ll11.removeAllViews()
        ll22.removeAllViews()
        ll33.removeAllViews()
        ll44.removeAllViews()
    }

    fun draw(){

        for (i in arr.indices){
            for (j in 0 until arr[i]){
                if(i==0){
                    val img = ImageView(this)
                    img.layoutParams = LinearLayout.LayoutParams(80,80)
                    img.setBackgroundResource(R.drawable.full_circle)
                    ll11.addView(img)
                }
                if(i==1){
                    val img = ImageView(this)
                    img.layoutParams = LinearLayout.LayoutParams(80,80)
                    img.setBackgroundResource(R.drawable.full_circle)
                    ll22.addView(img)
                }
                if(i==2){
                    val img = ImageView(this)
                    img.layoutParams = LinearLayout.LayoutParams(80,80)
                    img.setBackgroundResource(R.drawable.full_circle)
                    ll33.addView(img)
                }
                if(i==3){
                    val img = ImageView(this)
                    img.layoutParams = LinearLayout.LayoutParams(80,80)
                    img.setBackgroundResource(R.drawable.full_circle)
                    ll44.addView(img)
                }
            }

        }

    }

    fun checkWinner(){
        if(computerLost){
            currentPlayer2.text = "Player wins"
            btnFinishMove2!!.visibility = View.GONE
            btnPlayAgain!!.visibility = View.VISIBLE
        }
        var x = 0
        for (i in arr.indices){
            if (arr[i]!=0)
                x++;
        }
        winner = x == 0
        if (winner){
            if(CP == "Player"){
                currentPlayer2.text = "Computer wins"
            }else{
                currentPlayer2.text = "Player wins"
            }
            btnFinishMove2!!.visibility = View.GONE
            btnPlayAgain!!.visibility = View.VISIBLE
        }
    }

    fun setLayoutGravity(){
        ll11.gravity = Gravity.CENTER_HORIZONTAL
        ll22.gravity = Gravity.CENTER_HORIZONTAL
        ll33.gravity = Gravity.CENTER_HORIZONTAL
        ll44.gravity = Gravity.CENTER_HORIZONTAL
    }

    data class State(val array: IntArray, val depth: Int)

    fun StateSpaceGraph(input: IntArray): Map<State, List<State>> {
        val graph = mutableMapOf<State, MutableList<State>>()

        fun dfs(state: State, visited: MutableSet<String>) {
            val stateKey = state.array.joinToString(separator = ",")

            if (visited.contains(stateKey)) return
            visited.add(stateKey)

            for (i in state.array.indices) {
                for (j in 1..state.array[i]) {
                    val newStateArray = state.array.copyOf()
                    newStateArray[i] -= j
                    val newState = State(newStateArray, state.depth + 1)

                    graph.getOrPut(state) { mutableListOf() }.add(newState)

                    dfs(newState, visited)
                }
            }
        }

        dfs(State(input, 0), mutableSetOf())

        for ((state, neighbors) in graph) {
            val stateString = state.array.joinToString(separator = ",")
            val neighborsString = neighbors.joinToString(separator = "; ") { it.array.joinToString(separator = ",") }
            Log.d("StateSpaceGraph", "State: [$stateString], Depth: ${state.depth}, Neighbors: [$neighborsString]")
        }

        return graph
    }
}