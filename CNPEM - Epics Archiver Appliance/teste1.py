import numpy as np
from chaco.shell import *
import urllib2
import json

data_retrieval_url = "http://localhost/lnls-archiver/data/getData.json?pv="
pv_name = "MBTemp2:Channel1".replace(':', '%3A')
initial_date = "&from=2016-02-22T07:30:00.000Z".replace(':', '%3A')

url_json = data_retrieval_url + pv_name + initial_date

req = urllib2.urlopen(url_json)
data = json.load(req)
secs = [x['secs'] for x in data[0]['data']]
vals = [x['val'] for x in data[0]['data']]
plot(secs, vals, "r-")
xscale('time')
show()