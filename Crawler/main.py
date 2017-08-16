from pages import *
import requests
from bs4 import BeautifulSoup
import urllib2
import re
import csv

import datetime
import os.path
import shutil


def main():

	# row = 'BRAND' + ';' + 'SKU' + ';' + 'PRECO_CHEIO' + ';' + 'PRECO_SALES' + ';' + 'isSale' + ';' + 'TAMANHO' + ';' + 'QTY_STOCK'
	# file = open('fem.csv', 'a')
	# file.write(row + '\n')
	# file.close()

	# pagesLink('https://seekingalpha.com/symbol/PBR/focus','PBR')
	pagesLink('https://seekingalpha.com/symbol/GOOG/focus','GOOG')
	pagesLink('https://seekingalpha.com/symbol/AAPL/focus','AAPL')
	pagesLink('https://seekingalpha.com/symbol/FB/focus','FB')
	pagesLink('https://seekingalpha.com/symbol/GM/focus','GM')
	pagesLink('https://seekingalpha.com/symbol/BBY/focus','BBY')
	pagesLink('https://seekingalpha.com/symbol/IBM/focus','IBM')
	pagesLink('https://seekingalpha.com/symbol/OGS/focus','OGS')
	pagesLink('https://seekingalpha.com/symbol/USG/focus','USG')
	pagesLink('https://seekingalpha.com/symbol/IRT/focus','IRT')
	pagesLink('https://seekingalpha.com/symbol/FRTA/focus','FRTA')
	pagesLink('https://seekingalpha.com/symbol/DY/focus','DY')
	
main()