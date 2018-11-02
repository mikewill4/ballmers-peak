
import sqlite3 as sql

def get_db(database_name):
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sql.connect(database_name)
    return db

def query_db(query, database_name):
    try:
         with sql.connect(database_name) as con:
            cur = con.cursor()
            result = cur.execute(query)

            return result.fetchall()
    except:
       con.rollback()
       print("Error executing query: " + query)
       return new list()