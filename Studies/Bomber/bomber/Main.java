package bomber;

/*@nullable_by_default@*/
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Point p = new Point(20, 20);
		Blast b = new Blast(); 
		b.m_pos = p;
		for (int i = 0; i < 1000; i++) {
			b.getRadius();
			b.getDamage(1);	
			b.handle(10);
		}
	}

}
