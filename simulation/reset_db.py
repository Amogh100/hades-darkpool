import psycopg2
import psycopg2.extras

import os

username = os.environ['POSTGRES_USERNAME']
password = os.environ['POSTGRES_PASSWORD']

conn = psycopg2.connect(dbname="hadesmaster", user=username, password=password, host="/var/run/postgresql")
cur = conn.cursor()

#Truncate trades and orders
cur.execute("TRUNCATE trade, orders")

#Reset Capital
cur.execute("UPDATE account SET capital = 1000000000000")

conn.commit()
