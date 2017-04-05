#!/usr/bin/python

"""
This example shows how to work with WiFi on Mininet.
"""

from mininet.net import Mininet
from mininet.node import  OVSController, OVSKernelSwitch
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink

def topology():
    "Create a network."
    net = Mininet( controller=OVSController, link=TCLink, switch=OVSKernelSwitch )
	
    _bw=10
    _latency='5ms'
    _max_queue_size=100
    _use_htb=True
    # _ip_remote_control='127.0.0.1'
    # _port_remote_control=6653

    print "*** Creating nodes"
    h1 = net.addHost( 'h1', mac="00:00:00:00:00:01", ip='10.0.0.1' )

    h2 = net.addHost( 'h2', mac="00:00:00:00:00:02", ip='10.0.0.2' )
    h3 = net.addHost( 'h3', mac="00:00:00:00:00:03", ip='10.0.0.3' )
    h4 = net.addHost( 'h4', mac="00:00:00:00:00:04", ip='10.0.0.4' )

    h5 = net.addHost( 'h5', mac="00:00:00:00:00:05", ip='10.0.0.5' )
    h6 = net.addHost( 'h6', mac="00:00:00:00:00:06", ip='10.0.0.6' )


    s1 = net.addSwitch( 's1' )
    s2 = net.addSwitch( 's2' )
    s3 = net.addSwitch( 's3' )
    c0 = net.addController('c0', controller=OVSController)

    

    print "*** Associating Stations"
    net.addLink(h1, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)

    net.addLink(h2, s1, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h3, s1, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h4, s1, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h5, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h6, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    
    net.addLink(s1, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s1, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    
    print "*** Starting network"
    net.build()
    c0.start()
    s1.start( [c0] )
    s2.start( [c0] )
    s3.start( [c0] )

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


