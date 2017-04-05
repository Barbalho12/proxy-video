

public class Map {
	
	Machine machine;
	MediaServer mediaServer;
	double bitrate;
	String name;
	
	
	public Map(Machine machine, MediaServer mediaServer) {
		super();
		this.machine = machine;
		this.mediaServer = mediaServer;
	}

	public double getBitrate() {
		return bitrate;
	}

	public void setBitrate(double bitrate) {
		this.bitrate = bitrate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public MediaServer getMediaServer() {
		return mediaServer;
	}

	public void setMediaServer(MediaServer mediaServer) {
		this.mediaServer = mediaServer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((machine == null) ? 0 : machine.hashCode());
		result = prime * result + ((mediaServer == null) ? 0 : mediaServer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Map other = (Map) obj;
		if (machine == null) {
			if (other.machine != null)
				return false;
		} else if (!machine.equals(other.machine))
			return false;
		if (mediaServer == null) {
			if (other.mediaServer != null)
				return false;
		} else if (!mediaServer.equals(other.mediaServer))
			return false;
		return true;
	}

	

}
