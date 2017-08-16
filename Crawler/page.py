# -*- coding: utf-8 -*-
import re
import sys
import requests
from bs4 import BeautifulSoup
import urllib2
import re
import csv
import unicodedata as ud
import datetime
import os.path
import shutil
import datetime


# def teste():

	# link = 'https://seekingalpha.com/article/4091321-google-home-competitive-strengths-fast-growing-smart-speaker-market'

	
	
def teste(link,brand):
	#brand = 'TESTE'

	opener = urllib2.build_opener()
	opener.addheaders = [('User-agent', 'Mozilla/5.0')]
	response = opener.open(link)
	
	file = open('acao1.csv', 'a')
	
	
	soup = BeautifulSoup(response, 'lxml')
	
	#get Title
	seila5 = soup.find('h1',{'class':'has-title-test'})
	asd5 = seila5.text
	print ('===========================================')
	print asd5.encode('utf-8')
	file.write(asd5.encode('utf-8') + ';')
	
	
	#get the date Published
	seila4 = soup.find('time',{'itemprop':'datePublished'})
	asd4 = seila4.text
	print ('===========================================')
	# print asd4.encode('utf-8')
	file.write(asd4.encode('utf-8') + ';')
	
	
	#get the author name
	seila3 = soup.find('div',{'class':'top'})
	asd3 =	 seila3.text
	print ('===========================================')
	# print asd3.encode('utf-8')
	file.write(asd3.encode('utf-8') + ';')
	
	#get summary
	seila = soup.find('div',{'class': 'article-summary article-width'})
	asd = seila.text
	
	asd = asd.replace(';', ' ')
	asd = asd.replace('\n', ' ')
	# print asd.encode('utf-8')
	file.write(asd.encode('utf-8') + ';')
	
	#get analysis
	seila2 = soup.find('div',{'class': 'sa-art article-width'})
	asd2 = seila2.text
	asd2 = asd2.replace(';', ' ')
	asd2 = asd2.replace('\n', ' ')
	asd2 = asd2.replace('\n\nDisclosure: I/we have no positions in any stocks mentioned, and no plans to initiate any positions within the next 72 hours.', '')
	asd2 = asd2.replace('\nI wrote this article myself, and it expresses my own opinions. I am not receiving compensation for it (other than from Seeking Alpha). I have no business relationship with any company whose stock is mentioned in this article.','')
	# print asd2.encode('utf-8')
	file.write(asd2.encode('utf-8') + '\n')
	
	
	
# teste()
	
