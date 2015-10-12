package el3;

/**
 * Cette classe permet de mesurer des durées.
 */
public class Chrono {
    /** date de début du chronométrage, en millisecondes. */
    private long start_time;

    /** durée en milliseconde du dernier chronométrage. */
    private long elapsed_time;

    /**
     * cumul des durées des chronométrages effectués depuis la 
     * création du chronomètre.
     */
    private long total_elapsed;

    /**
     * Crée un chronomètre initialement arrêté.
     */
    public Chrono() {
        this.start_time = 0;
        this.elapsed_time = 0;
        this.total_elapsed = 0;
    }

    /**
     * Démarre le chronométrage.
     * Si cette méthode est invoquée plusieurs fois de suite, seule
     * la dernière invocation compte.
     */
    public void start() {
        this.start_time = System.currentTimeMillis();
    }

    /**
     * Arrête le chronométrage.
     * Calcule la durée écoulée depuis la dernière invocation de 
     * <code>start</code>, ainsi que le temps total mesuré par
     * le chronomètre depuis sa création.
     * Un chronomètre arrêté peur être redémarré en invoquant sa méthode
     * <code>start</code>, la nouvelle durée mesurée venant se cumuler
     * à la durée totale déjà chronométrée.
     */
    public void stop() {
        this.elapsed_time = System.currentTimeMillis() - this.start_time;
        this.total_elapsed += this.elapsed_time;
    }

    /**
     * Affiche la durée écoulée entre les dernières invocations de
     * <code>start</code> et <code>stop</code>.
     */
    @Override
	public String toString() {
        long ms = this.elapsed_time % 1000;
        long s = (this.elapsed_time / 1000) % 60;
        long m = (this.elapsed_time / 60000) % 60;
        long h = (this.elapsed_time / 3600000);
        return Long.toString(h)+"h "+m+"mn "+s+"s "+ms+"ms";
    }

    /**
     * Affiche le total des durées écoulées entre toutes les invocations de
     * <code>start</code> et <code>stop</code> depuis la création
     * du chronomètre.
     */
    public String total() {
        long ms = this.total_elapsed % 1000;
        long s = (this.total_elapsed / 1000) % 60;
        long m = (this.total_elapsed / 60000) % 60;
        long h = (this.total_elapsed / 3600000);
        return Long.toString(h)+"h "+m+"mn "+s+"s "+ms+"ms";
    }
}

