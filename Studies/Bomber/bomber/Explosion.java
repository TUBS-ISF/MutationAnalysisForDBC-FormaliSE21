package bomber;

public class Explosion implements Visual
{
    protected Point[]            m_explosion;                  // explosion (or smoke) for object
    protected int[]              m_explosion_size;             // size of the explosion particle (0 for empty)
    protected int[]              m_explosion_color;            // color of the explosion
    protected short[]            m_explosion_direction;        // directon if which particle is flying
    protected byte[]             m_explosion_speed;            // speed with which each particle is flying
    protected int                m_explosion_initial_size;     // inital size for smoke
    protected int                m_decay;                      // how fast does explosion decay
    protected int                m_x, m_y;                     // center of explosion
    protected short              m_size;                       // number of alive particles
    protected int[]              m_color_table;                // color fo the current particle
    protected int                m_direction;                  // direction, in which explosion is going
    protected int                m_direction_dispersion;       // dispersion of direction
    protected byte               m_speed;                      // speed of explosion particles
    protected byte               m_speed_dispersion;           // dispersion of speed of explosion particles
    
    protected static final byte PARTICLE_SPEED = 30;
    
    public Explosion(int [] color_table, int x, int y, byte particles, int explosion_initial_size, int decay, int direction, int direction_dispersion, byte speed, byte speed_dispersion)
    {
        m_direction_dispersion = direction_dispersion;
        m_direction = direction;
        m_speed = speed;
        m_speed_dispersion = speed_dispersion;
        init(color_table, x, y, particles, explosion_initial_size, decay);
    }
    
    public Explosion(int [] color_table, int x, int y, byte particles, int explosion_initial_size, int decay) 
    {
        m_direction_dispersion = -1;        // no direction explosion
        m_speed_dispersion = -1;
        init(color_table, x, y, particles, explosion_initial_size, decay);
    }
    
    public void init(int [] color_table, int x, int y, byte particles, int explosion_initial_size, int decay) 
    {
        m_explosion = new Point[particles];
        m_explosion_size = new int[particles];
        m_explosion_color = new int[particles];
        m_explosion_direction = new short[particles];
        m_explosion_speed = new byte[particles];
        m_size = particles;
        m_explosion_initial_size = explosion_initial_size;
        m_decay = decay;
        m_x = x;
        m_y = y;
        m_color_table = color_table;
        for (int i = 0; i < m_explosion.length; i++) 
        {
            m_explosion[i] = new Point();
            newParticle(i);
        }
    }
    
    protected void newParticle(int index)
    {

    }
	
	public int handle(int delta){
		return 1;
	}
    
	public void draw(int x, int y){
	
	}
}
