package me.aneleu.diceplugin;

import org.jetbrains.annotations.NotNull;

public class RotateUtil {

    public static double @NotNull [] rotateDotByVector(double[] point, double[] origin, double[] axis, double theta) {

        double u = axis[0];
        double v = axis[1];
        double w = axis[2];

        double magnitude = Math.sqrt(u*u + v*v + w*w);
        if (magnitude != 0) {
            u /= magnitude;
            v /= magnitude;
            w /= magnitude;
        } else {
            w = 1;
        }

        double x = point[0] - origin[0];
        double y = point[1] - origin[1];
        double z = point[2] - origin[2];

        double[] result = new double[3];
        double v1 = u * x + v * y + w * z;
        result[0] = (u * v1 * (1 - Math.cos(theta)) + x*Math.cos(theta) + (-w*y + v*z)*Math.sin(theta)) + origin[0];
        result[1] = (v * v1 * (1 - Math.cos(theta)) + y*Math.cos(theta) + (w*x - u*z)*Math.sin(theta)) + origin[1];
        result[2] = (w * v1 * (1 - Math.cos(theta)) + z*Math.cos(theta) + (-v*x + u*y)*Math.sin(theta)) + origin[2];

        return result;
    }

}
