import requests
from bs4 import BeautifulSoup
import urllib2
import csv
import datetime
from page import *

def pagesLink(url, brand):
	
	
	
	opener = urllib2.build_opener()
	opener.addheaders = [('User-agent', 'Mozilla/5.0')]
	response = opener.open(url)
	soup = BeautifulSoup(response, 'lxml')


	pageview = soup.find('div', {'class':'portfolio_selections summary'})
	
	try:
		pages = pageview.find_all('a')
		# print pages
		
		# pages2 = pages.find('a').get('href')
		for page in pages:
			a = find_between(str(page),'href="','"')
			if ('/article/' in a): 
				teste('https://seekingalpha.com' + a,brand)
				print 'https://seekingalpha.com' + a
			
			
	except:
		print ('ops')
	# try:
		# first = products[0].find('a').get('href')
		# print first
		# if (first == y):
			# x = False
		# else:
			# y = first
			# for product in products:
				# link = product.find('a').get('href')
				# link = str(link)
				# print link
				# teste(link,brand)
				# counter = counter + 1
				# print counter
			# page = page + 1
	# except:
		# x = False

	# counter = counter - 1
	# now = datetime.datetime.now()
	# row = brand + ';' + str(counter) + ';' + str(now.day)
	# file = open('total_items.csv', 'a')
	# file.write(row + '\n')
	# file.close()
			


def find_between( s, first, last ):
    try:
        start = s.index( first ) + len( first )
        end = s.index( last, start )
        return s[start:end]
    except ValueError:
        return ''
