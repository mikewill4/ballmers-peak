from flask import Flask, render_template, request, g
import sqlite3 as sql
app = Flask(__name__)

DATABASE = 'Teams.db'

@app.route('/registerteam')
def new_team():
   return render_template('register.html')

@app.route('/questions')
def questions():
   return render_template('questions.html')

@app.route("/")
def main():
    cur = get_db().cursor()
    return render_template("home.html")

@app.route("/ctf1")
def ctf1():
    cur = get_db().cursor()
    return render_template("ctf1.html")

@app.route("/ctf2")
def ctf2():
    cur = get_db().cursor()
    return render_template("ctf2.html")

@app.route("/algo1")
def algo1():
  return render_template("StockPrices.html")

@app.route("/algo2")
def algo2():
  return render_template("Skiing.html")

@app.route("/algo3")
def algo3():
  return render_template("KidsLiningUp.html")

@app.route("/algo4")
def algo4():
  return render_template("RoundingFloats.html")

@app.route("/algo5")
def algo5():
  return render_template("6dMahjong.html")

@app.route("/algo6")
def algo6():
  return render_template("AssigningToBrainCells.html")

@app.route("/algo7")
def algo7():
  return render_template("LogOrders.html")

@app.route('/leaderboard')
def leaderboard():
    try:
         with sql.connect("Teams.db") as con:
            cur = con.cursor()
            result = cur.execute("SELECT NAME, SCORE FROM REGISTERED ORDER BY SCORE DESC")

            return render_template('leaderboard.html', data = result.fetchall())
    except:
       con.rollback()
       return "Error fetching leaderboard. fuck bruh"


def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sql.connect(DATABASE)
    return db

@app.route('/addrec',methods = ['POST', 'GET'])
def addrec():
   if request.method == 'POST':
      try:
         nm = request.form['nm']
         pin = request.form['pin']
         
         with sql.connect("Teams.db") as con:
            cur = con.cursor()
            
            cur.execute("INSERT INTO REGISTERED (NAME,PASSWORD) VALUES (?,?)",(nm,pin) )
            
            con.commit()
            msg = "Record successfully added"
      except:
         con.rollback()
         msg = "error in insert operation"
      
      finally:
         return render_template("result.html",msg = msg)
         con.close()

@app.route('/registermember')
def new_mem():
   return render_template('new_member.html')

@app.route('/addmem',methods = ['POST', 'GET'])
def addmem():
   if request.method == 'POST':
      try:
         player_name = request.form['name']
         team = request.form['team']
         pin = request.form['pin']
   
         with sql.connect("Teams.db") as con:
            cur = con.cursor()

            result = cur.execute("SELECT * FROM REGISTERED WHERE NAME = (?) AND PASSWORD = (?)", (team, pin)) 
            num = 0
            
            for res in result:
                num += 1


            if num > 0 and num < 3:    
              cur.execute("INSERT INTO TEAM_MEMBERS (NAME,TEAM_NAME) VALUES (?,?)",(player_name,team))
              con.commit()
              msg = "Record successfully added"
            elif num == 0:
              msg = "Invalid team credentials"
            else:
              msg = "Team size is out of bounds"
      except:
         con.rollback()
         msg = "Failed to add team"
      
      finally:
         return render_template("result.html",msg = msg)
         con.close()

if __name__ == '__main__':
   app.run(debug = True)
