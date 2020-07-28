/*
 * Copyright 2020 Zetyun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zetyun.streamtau.manager.citrus;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.junit.JUnit4CitrusTest;
import com.zetyun.streamtau.manager.citrus.behavior.Assets;
import com.zetyun.streamtau.manager.citrus.behavior.Files;
import com.zetyun.streamtau.manager.citrus.behavior.Jobs;
import com.zetyun.streamtau.manager.citrus.behavior.Projects;
import com.zetyun.streamtau.manager.controller.protocol.JobRequest;
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import com.zetyun.streamtau.manager.db.model.JobStatus;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static com.zetyun.streamtau.manager.citrus.CitrusCommon.createPeasInList;
import static com.zetyun.streamtau.manager.citrus.CitrusCommon.deletePeasInList;
import static com.zetyun.streamtau.manager.citrus.CitrusCommon.getSortedAssetList;
import static com.zetyun.streamtau.manager.citrus.CitrusCommon.varRef;
import static com.zetyun.streamtau.manager.helper.ResourceUtils.readJobDef;

public class JavaJarAppIT extends JUnit4CitrusTest {
    @Test
    @CitrusTest
    public void testRun(@CitrusResource @NotNull TestDesigner designer) throws IOException {
        String projectId = "test";
        designer.applyBehavior(new Projects.Create(
            projectId,
            new ProjectRequest("test", "for citrus", "CONTAINER")
        ));
        JobDefPod pod = readJobDef("/jobdef/javajar/jar_app.json");
        List<AssetPea> peaList = getSortedAssetList(pod.getPeaMap());
        createPeasInList(designer, peaList, projectId);
        designer.applyBehavior(new Files.Upload(
            projectId,
            "JAR",
            new ClassPathResource("streamtau-test-hello-world.jar")
        ));
        designer.applyBehavior(new Jobs.Create(
            projectId,
            "job",
            new JobRequest("test", varRef(Assets.idVarName(pod.getAppId())), JobStatus.READY)
        ));
        deletePeasInList(designer, peaList, projectId);
        designer.applyBehavior(new Projects.Delete(projectId));
    }
}
