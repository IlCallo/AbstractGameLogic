public class RandomPoint {
	public static GeoLocation generate(ArrayList<GeoLocation> perimeter) {
		Path2D region = new Path2D.Double();

		region.moveTo(perimeter.get(0).latitude, perimeter.get(0).longitude);
		for (int idx = 1; idx < perimeter.size(); idx++) {
			region.lineTo(perimeter.get(idx).latitude, perimeter.get(idx).longitude);
		}
		region.closePath();
		
		Rectangle r = region.getBounds();
		double x, y;
		do {
			x = r.getX() + r.getWidth() * Math.random();
			y = r.getY() + r.getHeight() * Math.random();
		} while (!region.contains(x, y));
		
		return new GeoLocation(x, y);
	}
}