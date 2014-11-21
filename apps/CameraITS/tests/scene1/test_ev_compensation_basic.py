# Copyright 2014 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import its.image
import its.device
import its.objects
import os.path
import pylab
import matplotlib
import matplotlib.pyplot
import numpy

def main():
    """Tests that EV compensation is applied.
    """
    NAME = os.path.basename(__file__).split(".")[0]

    with its.device.ItsSession() as cam:
        props = cam.get_camera_properties()

        evs = range(-4,5)
        lumas = []
        for ev in evs:
            # Re-converge 3A, and lock AE once converged. skip AF trigger as
            # dark/bright scene could make AF convergence fail and this test
            # doesn't care the image sharpness.
            cam.do_3a(ev_comp=ev, lock_ae=True, do_af=False)

            # Capture a single shot with the same EV comp and locked AE.
            req = its.objects.auto_capture_request()
            req['android.control.aeExposureCompensation'] = ev
            req["android.control.aeLock"] = True
            cap = cam.do_capture(req)
            y = its.image.convert_capture_to_planes(cap)[0]
            tile = its.image.get_image_patch(y, 0.45,0.45,0.1,0.1)
            lumas.append(its.image.compute_image_means(tile)[0])

        pylab.plot(evs, lumas, 'r')
        matplotlib.pyplot.savefig("%s_plot_means.png" % (NAME))

        luma_diffs = numpy.diff(lumas)
        min_luma_diffs = min(luma_diffs)
        print "Min of the luma value difference between adjacent ev comp: ", \
                min_luma_diffs
        # All luma brightness should be increasing with increasing ev comp.
        assert(min_luma_diffs > 0)

if __name__ == '__main__':
    main()
