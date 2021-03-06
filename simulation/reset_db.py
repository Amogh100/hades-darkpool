import psycopg2
import psycopg2.extras

import os
'''
This script is just used to reset the database.
This removes all orders/trade from the database
and sets all capital levels to a large number.
This is useful for simulations.
'''

username = os.environ['POSTGRES_USERNAME']
password = os.environ['POSTGRES_PASSWORD']

conn = psycopg2.connect(dbname="hadesmaster", user=username, password=password, host="127.0.0.1")
cur = conn.cursor()

#Truncate trades and orders
cur.execute("TRUNCATE trade, orders")

#Reset Capital
cur.execute("UPDATE account SET capital = 1000000000000")

conn.commit()
