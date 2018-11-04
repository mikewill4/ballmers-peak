from flask import Flask, render_template, request, g
from DatabaseUtility import query_db
import sqlite3 as sql
app = Flask(__name__)

DATABASE = 'BallmersPeak.db'

@app.route('/registerteam')
def new_team():
   return render_template('register.html')

@app.route('/viewleaderboard')
def view_leaderboard():
  return render_template('view_leaderboard.html')

@app.route('/leaderboard', methods = ['POST'])
def leaderboard():
  msg = ''
  if request.method == 'POST':
    if True:
      gameID = request.form['id']
      with sql.connect("BallmersPeak.db") as con:
          cur = con.cursor()
          result = cur.execute("SELECT TeamName, Score FROM (SELECT TeamID, SUM(Score) as Score FROM (SELECT TeamID, questionID, MAX(Score) as Score FROM Submissions GROUP BY teamID, questionID HAVING GameID = (?))) INNER JOIN Teams USING (teamID) ORDER BY Score DESC;", (gameID)) 
          msg = '<br><b>  Team Name | Score </b><br>'
          
          for pair in result:
            msg += pair[0] + ' | ' + str(pair[1]) + '<br>' 
      
      print(msg)
    # except:
    #   con.rollback()
    #   msg = "Failed to retrieve leaderboard"
      
    # finally:
      con.close()
      return render_template("result.html",msg = msg)

@app.route('/teamregistered', methods = ['POST'])
def teamregistered():
  msg = ''
  if request.method == 'POST':
      try:
        team_name = request.form['name']
        gameID = int(request.form['id'])
        new_game_id = gameID

        ## Need to create a new gameID
        if gameID == -1:
          game_id_query_result = query_db('SELECT MAX(GameID) from Game;', 'BallmersPeak.db')
          if (len(game_id_query_result) == 0 or game_id_query_result[0][0] is None):
            new_game_id = 0
          else:
            new_game_id = game_id_query_result[0][0] + 1

          with sql.connect("BallmersPeak.db") as con:
            cur = con.cursor()
            result = cur.execute("INSERT INTO Game (GameID) VALUES (NULL)") 
            con.commit()

        team_id_query_result = query_db('SELECT MAX(TeamID) from Teams;', 'BallmersPeak.db')
        new_team_id = -1
        if (len(team_id_query_result) == 0 or team_id_query_result[0][0] is None):
          new_team_id = 0
        else:
          new_team_id = team_id_query_result[0][0] + 1
        
        with sql.connect("BallmersPeak.db") as con:
          cur = con.cursor()
          result = cur.execute("INSERT INTO Teams (TeamID, TeamName, GameID) VALUES (?, ?, ?)", (new_team_id, team_name, new_game_id)) 
          con.commit()

        msg = 'Thank you, ' + str(team_name) + ', your game id is: ' + str(new_game_id) + ', and your team id is: ' + str(new_team_id) + '.'
      except:
        con.rollback()
        msg = "Failed to add team"
      
      finally:
        con.close()
        return render_template("result.html",msg = msg)


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

@app.route("/ctf3")
def ctf3():
    cur = get_db().cursor()
    return render_template("ctf3.html")

@app.route("/ctf4")
def ctf4():
    cur = get_db().cursor()
    return render_template("ctf4.html")

@app.route("/ctf5")
def ctf5():
    cur = get_db().cursor()
    return render_template("ctf5.html")

@app.route("/ctf6")
def ctf6():
    cur = get_db().cursor()
    return render_template("ctf6.html")

@app.route("/ctf7")
def ctf7():
    cur = get_db().cursor()
    return render_template("ctf7.html")

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
