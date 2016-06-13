/*
 * minimal.c
 *
 *  Created on: 01/05/2013
 *      Author: valle
 */

// gcc minimal_exe6.c -o test -lGL -lGLU -lglut
// ./test


#if __APPLE__
#include <GLUT/glut.h>
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#else
#include <GL/glut.h>
#include <GL/gl.h>
#include <GL/glu.h>
#endif


#define v0 -1, -1,  1
#define v1  1, -1,  1
#define v2  1, -1, -1
#define v3 -1, -1, -1
#define v4  0, 0.8, 0

#define v0_c  0, 0, 1
#define v1_c  1, 0, 1
#define v2_c  0, 0, 1
#define v3_c  0, 0, 1
#define v4_c  1, 1, 1


static int camera = 0;

void draw_pyramid (void) {


	GLfloat vertices[] = {v0, v1, v2, v3, v4};
	GLfloat colors[]   = {v0_c, v1_c, v2_c, v3_c, v4_c};
	GLubyte indices[]  = {1,0,3, 3,2,1,
							1,4,2,
							4,3,2,
							3,4,0,
							0,4,1 };

	glEnableClientState(GL_COLOR_ARRAY);
	glEnableClientState(GL_VERTEX_ARRAY);

	glColorPointer(3, GL_FLOAT, 0, colors);
	glVertexPointer(3, GL_FLOAT, 0, vertices);

	glDrawElements(GL_TRIANGLES, 18, GL_UNSIGNED_BYTE, indices);

	glDisableClientState(GL_VERTEX_ARRAY);
	glDisableClientState(GL_COLOR_ARRAY);

}

void init(void) {
	glClearColor (0.0, 0.0, 0.0, 0.0);
	glShadeModel (GL_FLAT);
}


void display(void) {
	glClear (GL_COLOR_BUFFER_BIT);

	glPushMatrix();
	glRotatef ((GLfloat) camera, 0.0, 1.0, 0.0);
	glTranslatef(3, 0, 0);
	glScalef(2.0, 2, 2);
	draw_pyramid();
	glPopMatrix();

	glPushMatrix();
	glRotatef ((GLfloat) camera, 0.0, 1.0, 0.0);
	glTranslatef(0, 0, -2);
	glScalef(2.0, 1.5, 1);
	draw_pyramid();
	glPopMatrix();

	glPushMatrix();
	glRotatef ((GLfloat) camera, 0.0, 1.0, 0.0);
	draw_pyramid();
	glPopMatrix();

	glutSwapBuffers();
}


void reshape (int w, int h) {
	glViewport (0, 0, (GLsizei) w , (GLsizei) h);
	glMatrixMode (GL_PROJECTION);
	glLoadIdentity ();
	gluPerspective(90.0, (GLfloat) w/(GLfloat) h, 1.0, 20.0);
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
