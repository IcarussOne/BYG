import React, {useEffect, useState} from 'react';

declare const require: {
  context: (dir: string, deep: boolean, filter: RegExp) => {
    keys: () => string[];
    (key: string): string | {default: string};
  };
};

const screenshots = require.context('../../../assets/biomes', false, /^\.\/byg_.*\.png$/);
const choices = screenshots.keys().sort();

function imageFor(key: string): string {
  const module = screenshots(key);
  return typeof module === 'string' ? module : module.default;
}

function nameFor(key: string): string {
  return key.replace(/^\.\/byg_/, '').replace(/\.png$/, '').replace(/_/g, ' ');
}

export default function FooterLogo() {
  const [choice, setChoice] = useState(choices[0]);

  useEffect(() => {
    setChoice(choices[Math.floor(Math.random() * choices.length)]);
  }, []);

  if (!choice) return null;

  return (
    <figure className="footer-biome">
      <img src={imageFor(choice)} alt={`BYG ${nameFor(choice)} biome`} loading="lazy" />
      <figcaption>{nameFor(choice)}</figcaption>
    </figure>
  );
}
