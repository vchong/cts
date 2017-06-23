/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.location.cts.pseudorange;

import android.location.cts.pseudorange.Ecef2LlaConvertor.GeodeticLlaValues;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

/**
 * Transformations from ECEF coordiantes to Topocentric coordinates
 */
public class EcefToTopocentericConvertor {
  private static final double MIN_DISTANCE_MAGNITUDE_METERS = 1.0e-22;
  private static final int EAST_IDX = 0;
  private static final int NORTH_IDX = 1;
  private static final int UP_IDX = 2;

  /**
   * Transformation of {@code inputVectorMeters} with origin at {@code originECEFMeters} into
   * topocentric coordinate system. The result is {@code TopocentricAEDValues} containing azimuth
   * from north +ve clockwise, radians; elevation angle, radians; distance, vector length meters
   *
   * <p>Source: http://www.navipedia.net/index.php/Transformations_between_ECEF_and_ENU_coordinates
   * http://kom.aau.dk/~borre/life-l99/topocent.m
   *
   * @throws ArithmeticException
   */
  public static TopocentricAEDValues convertCartesianToTopocentericRadMeters(
      double[] originECEFMeters, double[] inputVectorMeters) throws ArithmeticException {

    GeodeticLlaValues latLngAlt = Ecef2LlaConvertor.convertECEFToLLACloseForm(originECEFMeters[0],
        originECEFMeters[1], originECEFMeters[2]);

    double sinLng = Math.sin(latLngAlt.longitudeRadians);
    double cosLng = Math.cos(latLngAlt.longitudeRadians);
    double sinLat = Math.sin(latLngAlt.latitudeRadians);
    double cosLat = Math.cos(latLngAlt.latitudeRadians);
    // Transform to ENU (East, north, up) local coordinate system
    double[][] matrixR3R1 = {{-sinLng, -cosLng * sinLat, cosLat * cosLng},
        {cosLng, -sinLat * sinLng, sinLng * cosLat},
        {0, cosLat, sinLat}};
    RealMatrix f = new Array2DRowRealMatrix(matrixR3R1);
    double[] eastNorthUpVectorMeters = GpsMathOperations.matrixByColVectMultiplication(
        f.transpose().getData(), inputVectorMeters);
    double eastMeters = eastNorthUpVectorMeters[EAST_IDX];
    double northMeters = eastNorthUpVectorMeters[NORTH_IDX];
    double upMeters = eastNorthUpVectorMeters[UP_IDX];

    // calculate azimuth, elevation and height from the ENU values
    double eastNorthDistanceMeters = Math.sqrt(Math.pow(eastMeters, 2) + Math.pow(northMeters, 2));
    double azimuthRadians;
    double elevationRadians;

    if (eastNorthDistanceMeters < MIN_DISTANCE_MAGNITUDE_METERS) {
      elevationRadians = Math.PI / 2.0;
      azimuthRadians = 0;
    } else {
      elevationRadians = Math.atan2(upMeters, eastNorthDistanceMeters);
      azimuthRadians = Math.atan2(eastMeters, northMeters);
    }
    if (azimuthRadians < 0) {
      azimuthRadians += 2 * Math.PI;
    }

    double distanceMeters = Math.sqrt(Math.pow(inputVectorMeters[0], 2)
        + Math.pow(inputVectorMeters[1], 2) + Math.pow(inputVectorMeters[2], 2));
    return new TopocentricAEDValues(elevationRadians, azimuthRadians, distanceMeters);
  }

  /**
   * Calculate azimuth, elevation in radians,and distance in meters between the user position in
   * ECEF meters {@code userPositionECEFMeters} and the satellite position in ECEF meters
   * {@code satPositionECEFMeters}
   */
  public static TopocentricAEDValues calculateElAzDistBetween2Points(
      double[] userPositionECEFMeters, double[] satPositionECEFMeters) {

    return convertCartesianToTopocentericRadMeters(userPositionECEFMeters,
        GpsMathOperations.subtractTwoVectors(satPositionECEFMeters, userPositionECEFMeters));

  }

  /**
   *
   * Class containing topocenter coordinates: azimuth in radians, elevation in radians, and distance
   * in meters
   */
  public static class TopocentricAEDValues {

    public final double elevationRadians;
    public final double azimuthRadians;
    public final double distanceMeters;

    public TopocentricAEDValues(double elevationRadians, double azimuthRadians,
        double distanceMeters) {
      this.elevationRadians = elevationRadians;
      this.azimuthRadians = azimuthRadians;
      this.distanceMeters = distanceMeters;
    }
  }
}
