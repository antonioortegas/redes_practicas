from manim import *


class Video(Scene):
    def construct(self):
        # Create a HTTP request visualisation using rectangles with rounded corners
        buff = 0.1
        opacity = 0.2
        # Method
        method = RoundedRectangle(height=1, width=2, corner_radius=0.1, fill_color=GREY, fill_opacity=opacity)
        url = RoundedRectangle(height=1, width=6, corner_radius=0.1, fill_color=GREY, fill_opacity=opacity)
        # Positioning url to the right of method
        url.next_to(method, RIGHT, buff=buff)
        # Version
        version = RoundedRectangle(height=1, width=3, corner_radius=0.1, fill_color=GREY, fill_opacity=opacity)
        version.next_to(url, RIGHT, buff=buff)
        
        # Put the 3 in a VGroup
        request_line = VGroup(method, url, version)
        
        # Headers
        width = method.get_width() + url.get_width() + version.get_width() + 2*buff
        headers = RoundedRectangle(height=3, width=width, corner_radius=0.1, fill_color=GREY, fill_opacity=opacity)
        headers.next_to(request_line, DOWN, buff=buff)
        
        # Blank line
        blank = RoundedRectangle(height=1, width=width, corner_radius=0.1, fill_color=GREY, fill_opacity=opacity)
        blank.next_to(headers, DOWN, buff=buff)
        
        # Body
        body = RoundedRectangle(height=3, width=width, corner_radius=0.1, fill_color=GREY, fill_opacity=opacity)
        body.next_to(blank, DOWN, buff=buff)
        
        # texts
        size = 24
        method_text = Text("GET", font="Monospace", font_size=size)
        method_text.move_to(method)
        url_text = Text("https://swapi.dev/api/...", font="Monospace", font_size=size)
        url_text.move_to(url)
        version_text = Text("HTTP/1.1", font="Monospace", font_size=size)
        version_text.move_to(version)
        headers_text = Text("...\n\nAccept: application/json\n\nUser-Agent: aos_swapi_client-2024", font="Monospace", font_size=size)
        headers_text.move_to(headers)
        blank_text = Text("\\r\\n", font="Monospace", font_size=size)
        blank_text.move_to(blank)
        body_text = Text("[ This is the body of the request ]", font="Monospace", font_size=size)
        body_text.move_to(body)
        
        texts = VGroup(method_text, url_text, version_text, headers_text, blank_text, body_text)
        
        # HTTP Request
        http_request = VGroup(request_line, headers, body, blank, texts)
        http_request.move_to(ORIGIN)
        http_request.scale(0.9)
        
        # animations
        self.play(Write(method))
        self.play(Write(url))
        self.play(Write(version))
        self.play(Write(headers))
        self.play(Write(blank))
        self.play(Write(body))
        
        self.play(Write(method_text))
        self.play(Write(url_text))
        self.play(Write(version_text))
        self.play(Write(headers_text))
        self.play(Write(blank_text))
        self.play(Write(body_text))
        
        
        self.wait(3)
        

#! RUNNING THE SCENE
# change index to select the desired quality, and preview to 1 or 0 to enable or disable the preview
# (yes, this is next level laziness, I just did not want to have to write the command every time I wanted to test the scene)
index = 5
preview = 1
# dictionary with the different quality options
qualityDictionary = {
    0: "low_quality",
    1: "medium_quality",
    2: "high_quality",
    3: "production_quality",
    4: "fourk_quality",
    5: "example_quality"
}
quality = qualityDictionary[index]

with tempconfig({"quality": quality,
                 "preview": preview,
                 "media_dir": "src/practica6_HTTP/video/media"
                 }):
    scene = Video()
    scene.render()