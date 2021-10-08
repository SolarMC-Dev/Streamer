/*
 * Streamer
 * Copyright © 2021 SolarMC Developers
 *
 * Streamer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Streamer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Streamer. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */

package gg.solarmc.streamer;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

final class TestContext implements TestTemplateInvocationContext {

    private final StreamFactory streamFactory;

    TestContext(StreamFactory streamFactory) {
        this.streamFactory = streamFactory;
    }

    @Override
    public String getDisplayName(int invocationIndex) {
        return "Testing with " + streamFactory;
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
        return List.of(
                new StreamFactoryParameterResolver(streamFactory),
                new MockitoExtension(),
                new BoxesVerificationExtension());
    }

    private static final class BoxesVerificationExtension implements AfterEachCallback {

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            assertDoesNotThrow((Executable) Boxes::obtainBool);
            assertDoesNotThrow((Executable) Boxes::obtainRef);
        }

    }

}
