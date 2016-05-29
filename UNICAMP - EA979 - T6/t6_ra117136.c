#include <GL/glut.h>
#include <GL/gl.h>
#include <GL/glu.h>

static int camera = 0, cameraX = 0;


void init(void) {
	glClearColor (0.0, 0.0, 0.0, 0.0);
	glShadeModel (GL_FLAT);
}


void display(void) {
	glClear (GL_COLOR_BUFFER_BIT);

	glPushMatrix();

	glRotatef ((GLfloat) camera, 1.0, 0.0, 0.0);
	glRotatef ((GLfloat) cameraX, 0.0, 1.0, 0.0);

	glColor3f(1.0f,1.0f,0.0f);
	glutWireSphere(1.0, 20, 16);

	/* Esfera 1 */
	glTranslatef(0, 0, 1.6);		/* Move coordenada em Z */
	glColor3f(1.0f,1.0f,1.0f);		/* Muda cor */
	glutWireSphere(0.4, 20, 16);	/* Desenha esfera no centro desta nova coordenada.*/
	glTranslatef(0, 0, -1.6);		/* Reverte translacao  */

	/* Esfera 2 - Mesmas etapas que a esfera anterior */
	glTranslatef(0, 0, -1.6);
	glColor3f(0.0f,1.0f,1.0f);
	glutWireSphere(0.4, 20, 16);
	glTranslatef(0, 0, 1.6);		/* Restaura sistema de coordenadas */

	/* Esfera 3 - Mesmas etapas que as esferas anteriores, soh que neste caso,
	 * translados em Y */
	glTranslatef(0, 1.6, 0);
	glColor3f(1.0f,0.0f,1.0f);
	glutWireSphere(0.4, 20, 16);
	glTranslatef(0, -1.6, 0);

	/* Esfera 4 - Mesmas etapas que as esferas anteriores */
	glTranslatef(0, -1.6, 0);
	glColor3f(0.0f,0.0f,1.0f);
	glutWireSphere(0.4, 20, 16);
	glTranslatef(0, 1.6, 0);

	glPopMatrix(); /* DOCUMENTACAO :
					Use glPushMatrix and glPopMatrix to save and restore
            		the untranslated coordinate system.
	 */

	glutSwapBuffers();

}


void reshape (int w, int h) {
	glViewport (0, 0, (GLsizei) w, (GLsizei) h);
	glMatrixMode (GL_PROJECTION);
	glLoadIdentity ();
	gluPerspective(60.0, (GLfloat) w/(GLfloat) h, 1.0, 20.0);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt (0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
}


void keyboard (unsigned char key, int x, int y) {
	switch (key) {
	case 'c':
		camera = (camera + 10) % 360;
		break;
	case 'C':
		camera = (camera - 10) % 360;
		break;
	case 'x':
		cameraX = (cameraX - 10) % 360;
		break;
	case 'X':
		cameraX = (cameraX + 10) % 360;
		break;
	default:
		return;
	}
	glutPostRedisplay();
}


int main(int argc, char** argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB);

	glutInitWindowSize (500, 500);
	glutInitWindowPosition (100, 100);
	glutCreateWindow (argv[0]);

	init();

	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);

	glutMainLoop();

	return 0;
}
